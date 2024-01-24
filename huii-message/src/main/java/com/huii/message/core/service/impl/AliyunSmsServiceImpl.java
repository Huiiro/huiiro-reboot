package com.huii.message.core.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huii.message.config.properties.SmsProperties;
import com.huii.message.core.entity.SmsResult;
import com.huii.message.core.service.SmsService;
import com.huii.message.exception.SmsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * aliyun 短信服务实现
 *
 * @author huii
 */
@Order
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnClass(Client.class)
public class AliyunSmsServiceImpl implements SmsService {

    private final SmsProperties smsProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    private Client createClient() {
        Config config = new Config().setAccessKeyId(smsProperties.getAccessKeyId())
                .setAccessKeySecret(smsProperties.getAccessKeySecret())
                .setEndpoint(smsProperties.getEndpoint());
        return new Client(config);
    }

    @Override
    public SmsResult send(String phones, String template, Map<String, String> params) {
        if (StringUtils.isBlank(phones)) {
            throw new SmsException("手机号不能为空");
        }
        if (StringUtils.isBlank(template)) {
            throw new SmsException("模板不能为空");
        }
        try {
            Client client = createClient();
            SendSmsResponse response = client.sendSmsWithOptions(new SendSmsRequest()
                    .setSignName(smsProperties.getSignName())
                    .setTemplateCode(template)
                    .setPhoneNumbers(phones)
                    .setTemplateParam(objectMapper.writeValueAsString(params)), new RuntimeOptions());
            SmsResult result = SmsResult.builder()
                    .status("OK".equals(response.getBody().getCode()))
                    .message(response.getBody().getMessage())
                    .response(objectMapper.writeValueAsString(response))
                    .build();
            log.debug("{}", result);
            return result;
        } catch (Exception e) {
            throw new SmsException(e.getMessage());
        }
    }
}
package com.huii.message.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huii.message.core.entity.SmsResult;
import com.huii.message.core.service.SmsService;
import com.huii.message.exception.SmsException;
import com.huii.message.config.properties.SmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Order
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnClass(SmsClient.class)
public class TencentSmsServiceImpl implements SmsService {

    private final SmsProperties smsProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    private SmsClient createClient() {
        Credential credential = new Credential(smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(smsProperties.getEndpoint());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(credential, "", clientProfile);
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
            SendSmsRequest req = new SendSmsRequest();
            Set<String> set = Arrays.stream(phones.split(",")).map(p -> "+86" + p).collect(Collectors.toSet());
            req.setPhoneNumberSet(ArrayUtil.toArray(set, String.class));
            if (CollUtil.isNotEmpty(params)) {
                req.setTemplateParamSet(ArrayUtil.toArray(params.values(), String.class));
            }
            req.setTemplateID(template);
            req.setSign(smsProperties.getSignName());
            req.setSmsSdkAppid(smsProperties.getSdkAppId());
            SendSmsResponse resp = createClient().SendSms(req);
            SmsResult.SmsResultBuilder builder = SmsResult.builder()
                    .status(true)
                    .message("send success")
                    .response(objectMapper.writeValueAsString(resp));
            for (SendStatus sendStatus : resp.getSendStatusSet()) {
                if (!"Ok".equals(sendStatus.getCode())) {
                    builder.status(false).message(sendStatus.getMessage());
                    break;
                }
            }
            log.debug("{}", builder.build());
            return builder.build();
        } catch (Exception e) {
            throw new SmsException(e.getMessage());
        }
    }
}

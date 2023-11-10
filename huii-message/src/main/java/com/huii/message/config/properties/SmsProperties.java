package com.huii.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 * sms
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 配置开启sms
     */
    private String enableSms = "false";

    /**
     * 配置节点
     * <p>阿里云 dysmsapi.aliyuncs.com</p>
     * <p>腾讯云 sms.tencentcloudapi.com</p>
     */
    private String endpoint;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * sms sign
     */
    private String signName;

    /**
     * tencent app id
     */
    private String sdkAppId;
}

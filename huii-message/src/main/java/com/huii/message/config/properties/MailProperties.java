package com.huii.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮箱配置
 * mail
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    /**
     * 配置开启mail
     */
    private String enableMail = "false";

    /**
     * host
     * <p>
     * 默认为smtp协议地址
     * 参考： pop3/smtp/imap
     * 163:  smtp.163.com
     * qq:   smtp.qq.com
     */
    private String host;

    /**
     * 端口
     * <p>
     * 163需配置端口为465/25
     * qq需配置端口为587
     */
    private Integer port;

    /**
     * 邮箱协议
     * <p>
     * 163需配置协议smtp
     */
    private String protocol;

    /**
     * 邮箱用户名
     */
    private String username;

    /**
     * 邮箱密码
     */
    private String password;

    /**
     * 发送者 eg: 【huii】
     */
    private String sender = "huii";
}

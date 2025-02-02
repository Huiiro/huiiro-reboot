package com.huii.auth.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录参数配置
 * config.login
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.login")
public class LoginProperties {

    /**
     * 最大错误尝试此时 开始需要验证码
     * 如果 authTries =0 表示不进行验证
     */
    private Integer authTries = 3;

    /**
     * 最大错误尝试次数 开始封停账号
     * 如果 errorTries =0 表示不进行验证
     */
    private Integer errorTries = 10;

    /**
     * 验证码重置时间 (min)
     */
    private Integer authWaitTime = 60;

    /**
     * 账号封禁时间 (min)
     */
    private Integer errorWaitTime = 10;

    /**
     * oauth2 登录成功重定向地址 默认为本地前端回调接口
     */
    private String defaultOauth2LoginCallbackUrl = "http://localhost:5173/oauth/redirect";

    /**
     * oauth2 授权服务器回调地址
     */
    private String defaultOauth2LoginRedirectUrl = "http://localhost:8080/callback";
}

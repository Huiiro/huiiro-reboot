package com.huii.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt参数配置
 * config.jwt
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.jwt")
public class JwtProperties {

    /**
     * 密钥
     */
    private String key = "jwtKey";

    /**
     * 签发者
     */
    private String issuer = "huii";

    /**
     * 单 token 默认使用方式 token有效时长：20（h）
     */
    private Integer single = 20;

    /**
     * 双 token accessToken 短 token token 有效时长：3（h）
     */
    private Integer access = 3;

    /**
     * 双 token refreshToken 长 token token 有效时长 168（h）
     */
    private Integer refresh = 168;

    /**
     * devToken token 有效时长：30（d）
     */
    private Integer dev = 30;

    /**
     * token 识别符
     */
    private String keyword = "user";

    /**
     * token 刷新时间
     */
    private Integer autoRefreshTime = 30;

    /**
     * 启用 devToken 默认关闭
     */
    private String enableDev = "false";

    /**
     * 启用双 token 默认关闭
     */
    private String enableDoubleToken = "false";


    /**
     * 保存 accessToken 至 redis 中
     * <p>单token情况下使用同一变量</p>
     */
    private String saveAccessToken = "true";

}

package com.huii.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 过滤器配置
 * config.filter
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.filter")
public class FilterProperties {

    /**
     * 开启xss过滤器 默认开启
     */
    private String enableXss = "true";

    /**
     * 开启重复提交过滤器 默认开启
     */
    private String enableRepeat = "true";

    /**
     * 开启限流拦截器 默认开启
     */
    private String enableRateLimit = "true";

    /**
     * 开启黑名单拦截器 默认开启
     */
    private String enableForbidden = "true";

    /**
     * xss配置 排除链接
     */
    private String urlPattens;

    /**
     * xss配置  匹配链接
     */
    private String excludes;
}

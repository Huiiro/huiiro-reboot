package com.huii.generator.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 生成信息配置
 *gen
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "gen")
public class GenProperties {

    /**
     * 作者名
     */
    private String authorName = "huii";

    /**
     * 包名
     */
    private String packageName = "com.huii";

    /**
     * 默认模板
     */
    private String defaultTemplate = "1";

    /**
     * 默认前端版本
     */
    private String defaultFrontendType = "2";

    /**
     * 默认数据库类型
     */
    private String defaultSqlType = "2";
}

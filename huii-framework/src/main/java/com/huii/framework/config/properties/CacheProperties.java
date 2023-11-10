package com.huii.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存配置
 * config.cache
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.cache")
public class CacheProperties {

    /**
     * 缓存时间
     */
    private Integer cacheHours = 48;

    /**
     * 启用Redis缓存
     */
    private String enableCache = "true";

    /**
     * 启用lockingRedisCacheWriter
     */
    private String cacheWriterWithLock = "false";

    /**
     * 启用分布式锁
     */
    private String enableLock = "false";

    /**
     * 锁名称
     */
    private String lockName = "REDIS-LOCK";

    /**
     * 锁时间
     */
    private Long lockExpire = 20000L;
}

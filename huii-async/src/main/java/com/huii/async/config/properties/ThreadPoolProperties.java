package com.huii.async.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池参数配置
 * config.thread-pool
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.thread-pool")
public class ThreadPoolProperties {

    /**
     * 核心线程数
     */
    private Integer corePoolSize = 10;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = 20;

    /**
     * 等待执行队列容量
     */
    private Integer queueCapacity = 100;

    /**
     * 空闲线程在线程数超过核心线程数时等待的时间
     */
    private Integer keepAliveSeconds = 60;

    /**
     * 线程池在关闭时等待线程终止的最大时间
     */
    private Integer awaitTerminationSeconds = 60;
}

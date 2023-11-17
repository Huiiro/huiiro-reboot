package com.huii.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.huii.common.utils.redis.RedisSerialFastJsonUtils;
import com.huii.framework.config.properties.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.time.Duration;

/**
 * redis 配置
 *
 * @author huii
 */
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final CacheProperties cacheProperties;

    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisSerialFastJsonUtils serializer = new RedisSerialFastJsonUtils(Object.class);
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnProperty(value = "config.cache.enableCache", havingValue = "true", matchIfMissing = true)
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheWriter writer;
        if ("true".equals(cacheProperties.getCacheWriterWithLock())) {
            writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
        } else {
            writer = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        }
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(cacheProperties.getCacheHours()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(createJsonSerializer()));
        return RedisCacheManager.builder(writer)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "config.cache.enableCache", havingValue = "false")
    public CacheManager localCacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    @ConditionalOnProperty(value = "config.cache.enableLock", havingValue = "true")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory factory) {
        return new RedisLockRegistry(factory, cacheProperties.getLockName(), cacheProperties.getLockExpire());
    }

    private RedisSerializer<Object> createJsonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }
}

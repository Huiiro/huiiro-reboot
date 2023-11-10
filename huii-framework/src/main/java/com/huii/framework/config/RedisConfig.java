package com.huii.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huii.common.utils.redis.RedisSerialUtils;
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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.time.Duration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisConnectionFactory factory;
    private final CacheProperties cacheProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisSerialUtils<Object> serializer = new RedisSerialUtils<>(Object.class, objectMapper);
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnProperty(value = "config.cache.enableCache", havingValue = "true", matchIfMissing = true)
    public RedisCacheManager redisCacheManager() {
        RedisCacheWriter writer;
        if("true".equals(cacheProperties.getCacheWriterWithLock())) {
            writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
        } else {
            writer = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        }
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(cacheProperties.getCacheHours()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
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
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory factory){
        return new RedisLockRegistry(factory, cacheProperties.getLockName(), cacheProperties.getLockExpire());
    }
}

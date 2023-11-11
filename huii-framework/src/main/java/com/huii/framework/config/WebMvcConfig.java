package com.huii.framework.config;

import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.framework.config.properties.FilterProperties;
import com.huii.framework.handler.ForbiddenIpInterceptor;
import com.huii.framework.interceptor.RateLimitInterceptor;
import com.huii.framework.interceptor.RepeatSubmitInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final FilterProperties filterProperties;
    private final RedisTemplateUtils redisTemplateUtil;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(1800L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        if ("true".equals(filterProperties.getEnableRateLimit())) {
            registry.addInterceptor(new RateLimitInterceptor(redisTemplateUtil)).addPathPatterns("/**");
            log.debug("RateLimitInterceptor enable");
        }
        if ("true".equals(filterProperties.getEnableRepeat())) {
            registry.addInterceptor(new RepeatSubmitInterceptor(redisTemplateUtil)).addPathPatterns("/**");
            log.debug("RepeatSubmitInterceptor enable");
        }
        if ("true".equals(filterProperties.getEnableForbidden())) {
            registry.addInterceptor(new ForbiddenIpInterceptor(redisTemplateUtil)).addPathPatterns("/**");
            log.debug("ForbiddenIpInterceptor enable");
        }
    }
}

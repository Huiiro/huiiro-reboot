package com.huii.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 系统配置
 *
 * @author huii
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationConfig {
}

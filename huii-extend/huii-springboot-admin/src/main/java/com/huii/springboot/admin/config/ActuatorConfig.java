package com.huii.springboot.admin.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * http监控 将http请求情况提交springboot-admin 开启http监控
 * 生成环境回存在一些性能问题，官方建议使用 zipKin等开源框架接入
 *
 * @author huii
 */
@Configuration
public class ActuatorConfig {

    @Bean
    public HttpExchangeRepository httpTraceRepository() {
        InMemoryHttpExchangeRepository repository = new InMemoryHttpExchangeRepository();
        // 默认保存1000条http请求记录
        repository.setCapacity(1000);
        return repository;
    }
}


package com.huii.async.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * 子线程继承配置
 *
 * @author huii
 */
@Configuration
@RequiredArgsConstructor
public class ThreadContextConfig {

    private final RequestContextFilter requestContextFilter;
    private final DispatcherServlet dispatcherServlet;

    @PostConstruct
    public void init() {
        requestContextFilter.setThreadContextInheritable(true);
        dispatcherServlet.setThreadContextInheritable(true);
    }
}

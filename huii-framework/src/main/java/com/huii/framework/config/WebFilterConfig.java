package com.huii.framework.config;

import com.huii.common.filter.RepeatableFilter;
import com.huii.common.filter.XssFilter;
import com.huii.framework.config.properties.FilterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器配置
 *
 * @author huii
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebFilterConfig {

    private final FilterProperties filterProperties;

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ConditionalOnProperty(value = "config.filter.enableXss", havingValue = "true")
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());
        registration.setName("xssFilter");
        registration.addUrlPatterns(StringUtils.split(filterProperties.getUrlPattens(), ","));
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        Map<String, String> initParameters = new HashMap<>(1);
        initParameters.put("excludes", filterProperties.getExcludes());
        registration.setInitParameters(initParameters);
        log.debug("XssFilter enable");
        return registration;
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ConditionalOnProperty(value = "config.filter.enableRepeat", havingValue = "true")
    public FilterRegistrationBean repeatSubmitRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatableFilter());
        registration.setName("repeatableFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        log.debug("RepeatFilter enable");
        return registration;
    }
}

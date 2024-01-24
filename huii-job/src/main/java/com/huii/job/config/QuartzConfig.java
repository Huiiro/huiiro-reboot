package com.huii.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Configuration
public class QuartzConfig {

    private static final String QUARTZ_CONFIG_PATH = "/quartz.properties";

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("APPLICATION-CONTEXT-KEY");
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        return factory;
    }

    @Bean
    @ConditionalOnResource(resources = QUARTZ_CONFIG_PATH)
    Properties quartzProperties() {
        try {
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_CONFIG_PATH));
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } catch (IOException e) {
            log.error("quartz配置文件读取失败");
        }
        return null;
    }
}

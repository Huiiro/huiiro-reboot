package com.huii.framework.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
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

    /**
     * 临时解决springboot3.x(tomcat core 10.x)以上版本 async task request对象回收问题
     */
    @Bean
    TomcatConnectorCustomizer disableFacadeDiscard() {
        return (connector) -> connector.setDiscardFacades(false);
    }
}

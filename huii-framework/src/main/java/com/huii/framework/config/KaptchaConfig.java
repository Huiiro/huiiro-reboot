package com.huii.framework.config;

import com.huii.auth.kaptcha.KaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 验证码配置
 * com.google.code.kaptcha.Constants
 *
 * @author huii
 */
@Configuration
public class KaptchaConfig {

    /**
     * 通过实现kaptchaService重写自定义生成规则
     * 并在此处配置
     */
    @Bean
    @Primary
    public KaptchaService kaptchaServiceDefault(KaptchaService kaptchaService) {
        return kaptchaService;
    }
}

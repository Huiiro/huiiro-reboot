package com.huii.auth.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;

/**
 * Kaptcha 实现接口
 */
public interface KaptchaService {

    /**
     * kaptcha自定义规则实现接口
     *
     * @return com.google.code.kaptcha.Producer
     */
    DefaultKaptcha kaptchaProvider();
}

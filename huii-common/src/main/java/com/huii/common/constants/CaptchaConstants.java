package com.huii.common.constants;

/**
 * @author huii
 */
public interface CaptchaConstants {

    /**
     * 滑动验证码校验允许误差
     */
    Integer CAPTCHA_SLIDE_ALLOW_DEVIATION = 3;

    /**
     * 点击验证码校验允许误差
     */
    Integer CAPTCHA_CLICK_ALLOW_WIDTH_DEVIATION = 52;

    /**
     * 点击验证码校验允许误差
     */
    Integer CAPTCHA_CLICK_ALLOW_HEIGHT_DEVIATION = 32;
}

package com.huii.common.constants;

/**
 * 验证码常量
 *
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
    Integer CAPTCHA_CLICK_ALLOW_WIDTH_DEVIATION = 50;

    /**
     * 点击验证码校验允许误差
     */
    Integer CAPTCHA_CLICK_ALLOW_HEIGHT_DEVIATION = 30;

    /**
     * 点击验证码单次生成字符数
     */
    Integer CAPTCHA_CLICK_TEXT_GEN_ALL = 5;

    /**
     * 点击验证码单次生成有效字符数
     */
    Integer CAPTCHA_CLICK_TEXT_GEN_EFFECT = 3;

    /**
     * 旋转验证码校验允许误差
     */
    Integer CAPTCHA_ROTATE_ALLOW_DEVIATION = 28;
}

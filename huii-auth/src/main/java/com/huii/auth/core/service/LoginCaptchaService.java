package com.huii.auth.core.service;

import com.huii.auth.core.entity.Captcha;
import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.PointDto;

import java.util.Map;

/**
 * 登录验证码服务
 *
 * @author huii
 */
public interface LoginCaptchaService {

    /**
     * 根据用户名校验验证码
     *
     * @param username username
     * @param entity   LoginEntity
     */
    void checkVerifyCode(String username, LoginEntity entity);

    /**
     * 校验滑动验证码
     *
     * @param key   key
     * @param value value
     */
    void checkSlideCode(String key, Integer value);

    /**
     * 校验点击文字验证码
     *
     * @param key    key
     * @param points PointDto[]
     */
    void checkClickTextCode(String key, PointDto[] points);

    /**
     * 校验旋转验证码
     *
     * @param imageKey  key
     * @param imageCode value
     */
    void checkRotateCode(String imageKey, Integer imageCode);

    /**
     * 获取文字验证码
     *
     * @param minute minute
     * @return key code
     */
    Map<String, Object> createTextCaptcha(Integer minute);

    /**
     * 获取计算验证码
     *
     * @param minute minute
     * @return key code
     */
    Map<String, Object> createCalcCaptcha(Integer minute);

    /**
     * 获取滑动验证码
     *
     * @param captcha captcha
     * @param minute  minute
     * @return captcha
     */
    Captcha createSlideCaptcha(Captcha captcha, Integer minute);

    /**
     * 获取点击文字验证码
     *
     * @param captcha captcha
     * @param minute  minute
     * @return captcha
     */
    Captcha createClickTextCaptcha(Captcha captcha, Integer minute);

    /**
     * 获取旋转验证码
     *
     * @param captcha captcha
     * @param minute  minute
     * @return captcha
     */
    Captcha createRotateCaptcha(Captcha captcha, Integer minute);

    /**
     * 获取手机登录验证码
     *
     * @param phone    phone
     * @param template template
     * @param minute   minute
     */
    void createLoginSmsCode(String phone, String template, Integer minute);

    /**
     * 获取邮箱登录验证码
     *
     * @param email    email
     * @param template template
     * @param minute   minute
     */
    void createLoginEmailCode(String email, String template, Integer minute);
}

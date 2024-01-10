package com.huii.auth.core.controller;

import com.alibaba.fastjson2.JSON;
import com.huii.auth.core.entity.Captcha;
import com.huii.auth.core.entity.PointDto;
import com.huii.auth.service.LoginCaptchaService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.annotation.RateLimit;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 验证码接口
 *
 * @author huii
 */
@Validated
@Anonymous
@RestController
@RequestMapping("/auth/v1/captcha")
@RequiredArgsConstructor
public class CaptchaController extends BaseController {

    private final LoginCaptchaService loginCaptchaService;

    /**
     * 生成文本验证码
     */
    @RateLimit
    @GetMapping("/gen/text")
    public R<Map<String, Object>> getTextKaptcha() {
        Map<String, Object> map = loginCaptchaService.createTextCaptcha(CacheConstants.DEFAULT_CACHE_TIME);
        return R.ok(map);
    }

    /**
     * 生成计算验证码
     */
    @RateLimit
    @GetMapping("/gen/calc")
    public R<Map<String, Object>> getCalcKaptcha() {
        Map<String, Object> map = loginCaptchaService.createCalcCaptcha(CacheConstants.DEFAULT_CACHE_TIME);
        return R.ok(map);
    }

    /**
     * 获取滑动图片验证码
     */
    @RateLimit
    @PostMapping("/gen/slide")
    public R<Captcha> getSlideCaptcha(@RequestBody(required = false) Captcha captcha) {
        return R.ok(loginCaptchaService.createSlideCaptcha(new Captcha(), CacheConstants.DEFAULT_CACHE_TIME));
    }

    /**
     * 校验滑动图片验证码
     */
    @PostMapping("/check/slide")
    public R<Void> checkSlideCaptcha(@NotBlank @RequestParam String imageKey,
                                     @NotNull @RequestParam Integer imageCode) {
        loginCaptchaService.checkSlideCode(imageKey, imageCode);
        return R.ok();
    }

    /**
     * 获取点击文字图片验证码
     */
    @RateLimit
    @PostMapping("/gen/click/text")
    public R<Captcha> getClickTextCaptcha(@RequestBody(required = false) Captcha captcha) {
        return R.ok(loginCaptchaService.createClickTextCaptcha(new Captcha(), CacheConstants.DEFAULT_CACHE_TIME));
    }

    /**
     * 校验点击文字图片验证码
     */
    @SneakyThrows
    @PostMapping("/check/click/text")
    public R<Void> checkClickTextCaptcha(@NotBlank @RequestParam String imageKey,
                                         @NotNull @RequestParam String clickValue) {
        String decoded = URLDecoder.decode(clickValue, StandardCharsets.UTF_8);
        PointDto[] points = JSON.parseArray(decoded).toArray(PointDto.class);
        loginCaptchaService.checkClickTextCode(imageKey, points);
        return R.ok();
    }


    /**
     * 短信验证码模板
     */
    @GetMapping("/gen/sms")
    public R<Void> getSmsCaptcha(@NotBlank(message = "{user.phone.not.blank}") String phone, @NotBlank String template) {
        loginCaptchaService.createLoginSmsCode(phone, template, CacheConstants.DEFAULT_CACHE_TIME);
        return sendSuccess();
    }

    /**
     * 邮箱验证码模板
     */
    @GetMapping("/gen/mail")
    public R<Void> getMailCaptcha(@NotBlank(message = "{user.email.not.blank}") String email, @NotBlank String template) {
        loginCaptchaService.createLoginEmailCode(email, template, CacheConstants.DEFAULT_CACHE_TIME);
        return sendSuccess();
    }
}

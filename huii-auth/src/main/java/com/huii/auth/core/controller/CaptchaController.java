package com.huii.auth.core.controller;

import com.huii.auth.core.entity.Captcha;
import com.huii.auth.service.LoginCaptchaService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.annotation.RateLimit;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@Anonymous
@RestController
@RequestMapping("/auth/v1/captcha")
@RequiredArgsConstructor
public class CaptchaController extends BaseController {

    private final LoginCaptchaService loginCaptchaService;
    private final Integer DEFAULT_MINUTE = 10;

    /**
     * 生成文本验证码
     */
    @RateLimit
    @GetMapping("/gen/text")
    public R<Map<String, Object>> getTextKaptcha() {
        Map<String, Object> map = loginCaptchaService.createTextCaptcha(DEFAULT_MINUTE);
        return R.ok(map);
    }

    /**
     * 生成计算验证码
     */
    @RateLimit
    @GetMapping("/gen/calc")
    public R<Map<String, Object>> getCalcKaptcha() {
        Map<String, Object> map = loginCaptchaService.createCalcCaptcha(DEFAULT_MINUTE);
        return R.ok(map);
    }

    /**
     * 获取滑动图片验证码
     */
    @RateLimit
    @PostMapping("/gen/slide")
    public R<Captcha> getSlideCaptcha(@RequestBody Captcha captcha) {
        return R.ok(loginCaptchaService.createSlideCaptcha(captcha, DEFAULT_MINUTE));
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
     * 短信验证码模板
     */
    @GetMapping("/gen/sms")
    public R<Void> getSmsCaptcha(@NotBlank(message = "{user.phone.not.blank}") String phone, @NotBlank String template) {
        loginCaptchaService.createLoginSmsCode(phone, template, DEFAULT_MINUTE);
        return sendSuccess();
    }

    /**
     * 邮箱验证码模板
     */
    @GetMapping("/gen/mail")
    public R<Void> getMailCaptcha(@NotBlank(message = "{user.email.not.blank}") String email, @NotBlank String template) {
        loginCaptchaService.createLoginEmailCode(email, template, DEFAULT_MINUTE);
        return sendSuccess();
    }
}

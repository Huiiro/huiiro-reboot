package com.huii.auth.core.controller;

import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginCaptchaService;
import com.huii.auth.service.LoginService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Anonymous
@RestController
@RequestMapping("/auth/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginCaptchaService loginCaptchaService;

    /**
     * 账号密码登录
     */
    @RequestMapping("/account")
    public R<LoginVo> accountLogin(@RequestBody @Validated AccountDto dto, HttpServletRequest request) {
        dto.setLoginType(LoginType.ACCOUNT);
        String username = loginService.getUsername(dto.getUsername());
        dto.setUsername(username);
        loginCaptchaService.checkVerifyCode(username, dto);
        LoginVo loginVo = loginService.accountLogin(dto, request);
        return R.ok(loginVo);
    }

    /**
     * 邮箱验证码登录
     */
    @RequestMapping("/email")
    public R<LoginVo> accountLogin(@RequestBody @Validated EmailDto dto, HttpServletRequest request) {
        dto.setLoginType(LoginType.EMAIL);
        LoginVo loginVo = loginService.emailLogin(dto, request);
        return R.ok(loginVo);
    }

    /**
     * 手机验证码登录
     */
    @RequestMapping("/sms")
    public R<LoginVo> accountLogin(@RequestBody @Validated SmsDto dto, HttpServletRequest request) {
        dto.setLoginType(LoginType.SMS);
        LoginVo loginVo = loginService.smsLogin(dto, request);
        return R.ok(loginVo);
    }
}

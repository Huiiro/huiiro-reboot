package com.huii.auth.core.controller;

import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.core.service.LoginCaptchaService;
import com.huii.auth.core.service.LoginSecurityService;
import com.huii.auth.core.service.LoginService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.LoginType;
import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 *
 * @author huii
 */
@Validated
@Anonymous
@RestController
@RequestMapping("/auth/v1/login")
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final LoginService loginService;
    private final LoginCaptchaService loginCaptchaService;
    private final LoginSecurityService loginSecurityService;

    /**
     * 获取用户权限信息
     * 获取路由: <link> com.huii.controller.SysMenuController.getRouteList </link>
     */
    @GetMapping("/info")
    public R<LoginVo> getInfo() {
        LoginVo loginVo = loginService.getInfo();
        return R.ok(loginVo);
    }

    /**
     * 账号密码登录
     */
    @PostMapping("/account")
    public R<LoginVo> accountLogin(@RequestBody @Validated AccountDto dto,
                                   HttpServletRequest request, HttpServletResponse response) {
        dto.setLoginType(LoginType.ACCOUNT);
        String username = loginService.getUsername(dto.getUsername());
        dto.setUsername(username);
        String password = loginSecurityService.decrypt(dto.getPassword());
        dto.setPassword(password);
        loginCaptchaService.checkVerifyCode(username, dto);
        LoginVo loginVo = loginService.accountLogin(dto, request, response);
        return R.ok(MessageUtils.message(ResType.USER_LOGIN_SUCCESS.getI18n()), loginVo);
    }

    /**
     * 邮箱验证码登录
     */
    @PostMapping("/email")
    public R<LoginVo> accountLogin(@RequestBody @Validated EmailDto dto,
                                   HttpServletRequest request, HttpServletResponse response) {
        dto.setLoginType(LoginType.EMAIL);
        LoginVo loginVo = loginService.emailLogin(dto, request, response);
        return R.ok(MessageUtils.message(ResType.USER_LOGIN_SUCCESS.getI18n()), loginVo);
    }

    /**
     * 手机验证码登录
     */
    @PostMapping("/sms")
    public R<LoginVo> accountLogin(@RequestBody @Validated SmsDto dto,
                                   HttpServletRequest request, HttpServletResponse response) {
        dto.setLoginType(LoginType.SMS);
        LoginVo loginVo = loginService.smsLogin(dto, request, response);
        return R.ok(MessageUtils.message(ResType.USER_LOGIN_SUCCESS.getI18n()), loginVo);
    }
}

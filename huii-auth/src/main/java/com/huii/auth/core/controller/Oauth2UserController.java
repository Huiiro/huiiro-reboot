package com.huii.auth.core.controller;

import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginSecurityService;
import com.huii.auth.service.LoginService;
import com.huii.auth.service.LoginUserOauthService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.LoginType;
import com.huii.system.domain.SysUserOauth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 oauth2 登录信息管理
 *
 * @author huii
 */
@Validated
@Anonymous
@RestController
@RequestMapping("/oauth/user")
@RequiredArgsConstructor
public class Oauth2UserController extends BaseController {

    private final LoginService loginService;
    private final LoginSecurityService loginSecurityService;
    private final LoginUserOauthService loginUserOauthService;

    /**
     * 查询用户授权情况
     */
    @GetMapping("/bind/status")
    public R<List<SysUserOauth>> getMyBindStatus() {
        List<SysUserOauth> list = loginUserOauthService.getMyBindStatus(getUserId());
        return R.ok(list);
    }

    /**
     * 用户取消授权
     * 仅删除本地数据 不会从授权服务器上删除数据
     * 后续将会添加删除授权服务器上数据的功能
     */
    @PostMapping("/bind/cancel/{provider}")
    public R<Void> cancelGrantOauth(@PathVariable String provider) {
        loginUserOauthService.cancelGrantOauth(getUserId(), provider);
        return R.ok("已取消授权");
    }

    /**
     * 绑定至已有账号
     */
    @PostMapping("/bind/has")
    public R<Object> bindAccountToMy(@RequestBody @Validated AccountDto dto, HttpServletRequest request) {
        dto.setLoginType(LoginType.ACCOUNT);
        String username = loginService.getUsername(dto.getUsername());
        dto.setUsername(username);
        String password = loginSecurityService.decrypt(dto.getPassword());
        dto.setPassword(password);
        LoginVo loginVo = loginService.accountLogin(dto, request);
        loginUserOauthService.bindUser(getUserId(), loginVo);
        return R.ok(loginVo);
    }

    /**
     * 绑定第三方账号至本账号
     */
    @PostMapping("/bind/new/{provider}")
    public R<Object> bindThirdAccountToThis(@PathVariable String provider) {
        return R.ok();
    }
}

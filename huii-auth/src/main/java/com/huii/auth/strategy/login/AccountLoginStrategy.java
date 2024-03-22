package com.huii.auth.strategy.login;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.token.AccountToken;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.core.service.LoginSuccessService;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.strategy.AbstractLoginStrategy;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Component;

/**
 * 账号密码登录策略
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class AccountLoginStrategy extends AbstractLoginStrategy {

    private final AuthenticationManager manager;
    private final LoginSuccessService loginSuccessService;
    private final RememberMeServices rememberMeServices;

    @Override
    public LoginVo login(LoginEntity loginEntity, HttpServletRequest request, HttpServletResponse response) {
        String remember = request.getParameter("rememberMe");
        Authentication authentication;
        boolean rememberMeFlag = false;
        if (remember != null && remember.equals("true")) {
            authentication = rememberMeServices.autoLogin(request, response);
            if (authentication.isAuthenticated()) {
                rememberMeFlag = true;
            }
        } else {
            AccountDto dto = (AccountDto) loginEntity;
            AccountToken token = new AccountToken(dto.getUsername(), dto.getPassword());
            authentication = getAuthentication(token, manager);
        }
        LoginVo vo = authenticate(authentication, loginSuccessService, request);
        if (rememberMeFlag) {
            rememberMeServices.loginSuccess(request, response, authentication);
        }
        return vo;
    }

    @Override
    public void afterPropertiesSet() {
        LoginStrategyFactory.register(LoginType.ACCOUNT.getKey(), this);
    }
}

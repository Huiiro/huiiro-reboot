package com.huii.auth.provider;

import com.huii.auth.core.entity.token.SmsToken;
import com.huii.auth.core.service.LoginProviderService;
import com.huii.auth.core.service.impl.UserDetailServiceImpl;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 手机登录认证核心
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final LoginProviderService providerService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsToken token = (SmsToken) authentication;
        String sms = (String) token.getPrincipal();
        String code = (String) token.getCredentials();
        LoginUser loginUser = (LoginUser) userDetailService.loadUserBySms(sms);
        loginUser.setLoginType(LoginType.SMS);
        loginUser.setPrinciple(loginUser.getUser().getPhone());
        providerService.checkLoginBans(loginUser);
        providerService.checkLoginSmsCode(loginUser, code);
        providerService.loginSuccessPreHandler(loginUser);
        return new SmsToken(loginUser, token.getCredentials(), loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsToken.class.isAssignableFrom(authentication);
    }
}

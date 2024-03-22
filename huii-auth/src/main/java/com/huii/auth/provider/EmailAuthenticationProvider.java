package com.huii.auth.provider;

import com.huii.auth.core.entity.token.EmailToken;
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
 * 邮箱登录认证核心
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final LoginProviderService providerService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailToken token = (EmailToken) authentication;
        String Email = (String) token.getPrincipal();
        String code = (String) token.getCredentials();
        LoginUser loginUser = (LoginUser) userDetailService.loadUserByEmail(Email);
        loginUser.setLoginType(LoginType.EMAIL);
        loginUser.setPrinciple(loginUser.getUser().getPhone());
        providerService.checkLoginBans(loginUser);
        providerService.checkLoginEmailCode(loginUser, code);
        providerService.loginSuccessPreHandler(loginUser);
        return new EmailToken(loginUser, token.getCredentials(), loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailToken.class.isAssignableFrom(authentication);
    }
}

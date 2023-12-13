package com.huii.auth.provider;

import com.huii.auth.core.entity.token.AccountToken;
import com.huii.auth.service.LoginProviderService;
import com.huii.auth.service.impl.UserDetailServiceImpl;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * account provider
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class AccountAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final LoginProviderService providerService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccountToken token = (AccountToken) authentication;
        String username = (String) token.getPrincipal();
        String password = (String) token.getCredentials();
        LoginUser loginUser = (LoginUser) userDetailService.loadUserByUsername(username);
        loginUser.setLoginType(LoginType.ACCOUNT);
        loginUser.setPrinciple(username);
        providerService.checkLoginBans(loginUser);
        providerService.checkLoginPassword(loginUser, password);
        providerService.loginSuccessPreHandler(loginUser);
        return new AccountToken(loginUser, token.getCredentials(), loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccountToken.class.isAssignableFrom(authentication);
    }
}

package com.huii.auth.provider;

import com.huii.auth.config.properties.Oauth2ClientProperties;
import com.huii.auth.core.entity.oauth2.Oauth2Client;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.core.entity.token.Oauth2Token;
import com.huii.auth.factory.Oauth2StrategyFactory;
import com.huii.auth.oauth2.Oauth2Login;
import com.huii.auth.service.LoginSuccessService;
import com.huii.auth.service.impl.UserDetailServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.domain.SysUserOauth;
import com.huii.common.core.model.LoginUser;
import com.huii.common.exception.BasicAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final Oauth2ClientProperties oauth2ClientProperties;
    private final LoginSuccessService loginSuccessService;
    private static final String pattern = "/callback/(\\w+)";
    private static final List<Oauth2Client> clients = new ArrayList<>();

    @PostConstruct
    public void initializeClients() {
        clients.addAll(oauth2ClientProperties.getClients());
        log.info("load oauth2 configuration:{}", clients.size());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Oauth2Token token = (Oauth2Token) authentication;
        HttpServletRequest request = (HttpServletRequest) token.getPrincipal();
        request.setAttribute("code", token.getCode());
        request.setAttribute("state", token.getState());
        request.setAttribute("scope", token.getScope());
        Oauth2Client client = getProvider(request);
        Oauth2Login strategy = Oauth2StrategyFactory.getStrategy(client.getProvider());
        String accessToken = strategy.getAccessToken(client, request);
        Oauth2User oauthUser = strategy.getOauthUser(client, accessToken);
        SysUserOauth oauthUserInfo = loginSuccessService.getOauthUserInfo(oauthUser);
        LoginUser loginUser;
        if (oauthUserInfo != null) {
            loginUser = (LoginUser) userDetailService.loadUserByUserId(oauthUserInfo.getUserId());
        } else {
            //创建临时用户绑定第三方信息
            SysUser sysUser = loginSuccessService.createUserEntity(oauthUser);
            loginSuccessService.createUserOauthEntity(sysUser.getUserId(), oauthUser);
            loginUser = (LoginUser) userDetailService.loadUserByUserId(sysUser.getUserId());
            //设置未绑定标志
            loginUser.setBindStatus(SystemConstants.STATUS_0);
        }

        return new Oauth2Token(loginUser, null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return Oauth2Token.class.isAssignableFrom(authentication);
    }

    private Oauth2Client getProvider(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (StringUtils.isEmpty(requestURI)) {
            throw new BasicAuthenticationException("请求uri异常");
        }
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(requestURI);
        if (matcher.find()) {
            String provider = matcher.group(1);
            for (Oauth2Client client : clients) {
                if (client.getProvider().equals(provider)) {
                    return client;
                }
            }
        }
        throw new BasicAuthenticationException("获取Oauth2认证信息失败");
    }
}

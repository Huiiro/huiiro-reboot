package com.huii.auth.strategy.login;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.Oauth2Dto;
import com.huii.auth.core.entity.token.Oauth2Token;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.core.service.LoginSuccessService;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.strategy.AbstractLoginStrategy;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

/**
 * Oauth2登录策略
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class Oauth2LoginStrategy extends AbstractLoginStrategy {

    private final AuthenticationManager manager;
    private final LoginSuccessService loginSuccessService;

    @Override
    public LoginVo login(LoginEntity loginEntity, HttpServletRequest request, HttpServletResponse response) {
        Oauth2Dto dto = (Oauth2Dto) loginEntity;
        Oauth2Token token = new Oauth2Token(request, null, dto.getCode(),
                dto.getState(), dto.getScope(), dto.getType(), loginEntity.getLoginType(),
                dto.getHasLoginAndDoBind(), dto.getBindId());
        return this.authenticate(token, manager, loginSuccessService, request);
    }

    @Override
    public void afterPropertiesSet() {
        //此处需要注册所有的Oauth2登录方式
        LoginStrategyFactory.register(LoginType.GITHUB.getKey(), this);
        LoginStrategyFactory.register(LoginType.GITEE.getKey(), this);
    }
}

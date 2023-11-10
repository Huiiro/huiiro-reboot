package com.huii.auth.strategy;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.token.EmailToken;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.service.LoginSuccessService;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码登录策略
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class EmailLoginStrategy extends AbstractLoginStrategy {

    private final AuthenticationManager manager;
    private final LoginSuccessService loginSuccessService;

    @Override
    public LoginVo login(LoginEntity loginEntity, HttpServletRequest request) {
        EmailDto loginBody = (EmailDto) loginEntity;
        EmailToken token = new EmailToken(loginBody.getEmail(), loginBody.getCode());
        return this.authenticate(token, manager, loginSuccessService, request);
    }

    @Override
    public void afterPropertiesSet() {
        LoginStrategyFactory.register(LoginType.EMAIL.getKey(), this);
    }
}

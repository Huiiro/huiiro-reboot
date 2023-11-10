package com.huii.auth.strategy.login;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.token.AccountToken;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.service.LoginSuccessService;
import com.huii.auth.strategy.AbstractLoginStrategy;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Override
    public LoginVo login(LoginEntity loginEntity, HttpServletRequest request) {
        AccountDto dto = (AccountDto) loginEntity;
        AccountToken token = new AccountToken(dto.getUsername(), dto.getPassword());
        return this.authenticate(token, manager, loginSuccessService, request);
    }

    @Override
    public void afterPropertiesSet() {
        LoginStrategyFactory.register(LoginType.ACCOUNT.getKey(), this);
    }
}

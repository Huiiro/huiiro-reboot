package com.huii.auth.strategy.login;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.token.SmsToken;
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
 * 手机验证码登录策略
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class SmsLoginStrategy extends AbstractLoginStrategy {

    private final AuthenticationManager manager;
    private final LoginSuccessService loginSuccessService;

    @Override
    public LoginVo login(LoginEntity loginEntity, HttpServletRequest request, HttpServletResponse response) {
        SmsDto dto = (SmsDto) loginEntity;
        SmsToken token = new SmsToken(dto.getSms(), dto.getCode());
        return this.authenticate(token, manager, loginSuccessService, request);
    }

    @Override
    public void afterPropertiesSet() {
        LoginStrategyFactory.register(LoginType.SMS.getKey(), this);
    }
}

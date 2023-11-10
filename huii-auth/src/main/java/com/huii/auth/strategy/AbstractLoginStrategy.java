package com.huii.auth.strategy;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginSuccessService;
import com.huii.common.core.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

/**
 * 登录策略抽象类
 *
 * @author huii
 */
public abstract class AbstractLoginStrategy implements LoginStrategy {

    @Override
    public abstract LoginVo login(LoginEntity loginEntity, HttpServletRequest request);

    /**
     * 核心认证方法 获取对应的token后调用不同的provider进行认证
     *
     * @param authentication        authentication
     * @param authenticationManager authenticationManager
     * @param loginSuccessService   loginSuccessService
     * @param request               request
     * @param args                  args
     * @return LoginVo
     */
    public LoginVo authenticate(Authentication authentication, AuthenticationManager authenticationManager,
                                LoginSuccessService loginSuccessService, HttpServletRequest request,
                                String... args) {
        Authentication authenticate;
        authenticate = authenticationManager.authenticate(authentication);
        return loginSuccessService.autoCreateToken((LoginUser) authenticate.getPrincipal(), request);
    }
}

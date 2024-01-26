package com.huii.auth.strategy;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginSuccessService;
import com.huii.common.core.model.LoginUser;
import com.huii.common.exception.BasicAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

/**
 * 登录策略抽象类
 *
 * @author huii
 */
public abstract class AbstractLoginStrategy implements LoginStrategy {

    /**
     * 登录抽象接口
     *
     * @param loginEntity loginEntity
     * @param request     request
     * @return loginVo
     */
    @Override
    public abstract LoginVo login(LoginEntity loginEntity, HttpServletRequest request, HttpServletResponse response);

    /**
     * 核心认证方法 获取对应的token后调用不同的provider进行认证
     * 认证成功后 获取返回登录成功的信息
     *
     * @param authentication        未认证的Authentication对象
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
        authenticate = getAuthentication(authentication, authenticationManager);
        return loginSuccessService.autoCreateToken((LoginUser) authenticate.getPrincipal(), request);
    }

    /**
     * 核心认证方法 传入已认证的Authentication获取登录成功后的返回信息
     *
     * @param authentication      已认证的Authentication对象 不需要manager对象
     * @param loginSuccessService loginSuccessService
     * @param request             request
     * @param args                args
     * @return LoginVo
     */
    public LoginVo authenticate(Authentication authentication, LoginSuccessService loginSuccessService,
                                HttpServletRequest request, String... args) {
        try {
            LoginUser principal = (LoginUser) authentication.getPrincipal();
            return loginSuccessService.autoCreateToken(principal, request);
        } catch (Exception e) {
            throw new BasicAuthenticationException("未经授权的登录行为");
        }
    }

    /**
     * 单独获取Authentication对象
     * 调用authenticationManager进行认证
     *
     * @param authentication        authentication
     * @param authenticationManager authenticationManager
     * @return alreadyAuthenticated
     */
    public Authentication getAuthentication(Authentication authentication, AuthenticationManager authenticationManager) {
        Authentication authenticate;
        authenticate = authenticationManager.authenticate(authentication);
        return authenticate;
    }
}

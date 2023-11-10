package com.huii.auth.service;

import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.common.core.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录成功处理服务
 *
 * @author huii
 */
public interface LoginSuccessService {

    /**
     * 更新用户登录信息
     *
     * @param loginUser loginUser
     * @param request   request
     */
    void updateUserLoginInfo(LoginUser loginUser, HttpServletRequest request);

    /**
     * 根据授权对象颁发token
     *
     * @param loginUser loginUser
     * @return token...
     */
    LoginVo createToken(LoginUser loginUser);

    /**
     * 自动登录方法 包含更新用户信息和授权
     *
     * @param loginUser loginUser
     * @param request   request
     * @return token...
     */
    LoginVo autoCreateToken(LoginUser loginUser, HttpServletRequest request);
}

package com.huii.auth.strategy;

import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;

/**
 * 登录策略接口
 *
 * @author huii
 */
public interface LoginStrategy extends InitializingBean {

    /**
     * 登录接口
     *
     * @param loginEntity loginEntity
     * @param request     request
     * @return loginVo
     */
    LoginVo login(LoginEntity loginEntity, HttpServletRequest request, HttpServletResponse response);
}

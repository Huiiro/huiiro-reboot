package com.huii.auth.service;

import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录服务
 *
 * @author huii
 */
public interface LoginService {

    /**
     * accountLogin
     *
     * @param loginBody loginBody
     * @param request   request
     * @return LoginVo
     */
    LoginVo accountLogin(AccountDto loginBody, HttpServletRequest request);

    /**
     * emailLogin
     *
     * @param loginBody loginBody
     * @param request   request
     * @return LoginVo
     */
    LoginVo emailLogin(EmailDto loginBody, HttpServletRequest request);

    /**
     * smsLogin
     *
     * @param loginBody loginBody
     * @param request   request
     * @return LoginVo
     */
    LoginVo smsLogin(SmsDto loginBody, HttpServletRequest request);

    /**
     * 获取用户唯一用户名
     *
     * @param username username
     * @return username
     */
    String getUsername(String username);
}

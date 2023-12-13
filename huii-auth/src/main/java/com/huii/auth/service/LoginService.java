package com.huii.auth.service;

import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.Oauth2Dto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录服务
 *
 * @author huii
 */
public interface LoginService {


    /**
     * load userInfo when login success
     * @return loginVo
     */
    LoginVo getInfo();
    /**
     * accountLogin
     *
     * @param dto     dto
     * @param request request
     * @return LoginVo
     */
    LoginVo accountLogin(AccountDto dto, HttpServletRequest request);

    /**
     * emailLogin
     *
     * @param dto     dto
     * @param request request
     * @return LoginVo
     */
    LoginVo emailLogin(EmailDto dto, HttpServletRequest request);

    /**
     * smsLogin
     *
     * @param dto     dto
     * @param request request
     * @return LoginVo
     */
    LoginVo smsLogin(SmsDto dto, HttpServletRequest request);

    /**
     * oauth2Login
     *
     * @param dto     dto
     * @param request request
     * @return LoginVo
     */
    LoginVo oauth2Login(Oauth2Dto dto, HttpServletRequest request);

    /**
     * oauth2LoginResponse
     *
     * @param loginVo loginVo
     * @param response response
     */
    void defaultOauth2LoginResponse(LoginVo loginVo, HttpServletResponse response);

    /**
     * 获取用户唯一用户名
     *
     * @param username username
     * @return username
     */
    String getUsername(String username);

}

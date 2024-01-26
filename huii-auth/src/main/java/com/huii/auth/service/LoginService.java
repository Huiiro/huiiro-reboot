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
     *
     * @return loginVo
     */
    LoginVo getInfo();

    /**
     * accountLogin
     *
     * @param dto      dto
     * @param request  request
     * @param response response
     * @return LoginVo
     */
    LoginVo accountLogin(AccountDto dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * emailLogin
     *
     * @param dto      dto
     * @param request  request
     * @param response response
     * @return LoginVo
     */
    LoginVo emailLogin(EmailDto dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * smsLogin
     *
     * @param dto      dto
     * @param request  request
     * @param response response
     * @return LoginVo
     */
    LoginVo smsLogin(SmsDto dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * oauth2Login
     *
     * @param dto      dto
     * @param request  request
     * @param response response
     * @return LoginVo
     */
    LoginVo oauth2Login(Oauth2Dto dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * oauth2登录成功返回结果
     *
     * @param loginVo  loginVo
     * @param response response
     */
    void defaultOauth2LoginResponse(LoginVo loginVo, HttpServletResponse response);

    /**
     * oauth2绑定成功返回结果
     *
     * @param loginVo  loginVo
     * @param response response
     */
    void defaultOauth2LoginBindResponse(LoginVo loginVo, HttpServletResponse response);

    /**
     * 构建返回参数
     *
     * @param loginVo  loginVo
     * @param response response
     * @param origin   origin
     */
    void buildOauth2LoginResponseByOrigin(LoginVo loginVo, HttpServletResponse response, String origin);

    /**
     * 获取用户唯一用户名
     *
     * @param username username
     * @return username
     */
    String getUsername(String username);

}

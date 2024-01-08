package com.huii.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huii.auth.config.properties.LoginProperties;
import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.Oauth2Dto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.service.LoginService;
import com.huii.auth.strategy.LoginStrategy;
import com.huii.common.constants.RegConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.mapper.SysUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final SysUserMapper sysUserMapper;
    private final LoginProperties loginProperties;

    @Override
    public LoginVo getInfo() {
        LoginUser loginUser = SecurityUtils.getPrincipal();
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getUserId());
        SysUser user = loginUser.getUser();
        user.setAvatar(sysUser.getAvatar());
        user.setUserName(sysUser.getUserName());
        return LoginSuccessServiceImpl.loginVoBuilder(new LoginVo(), loginUser);
    }

    @Override
    public LoginVo accountLogin(AccountDto dto, HttpServletRequest request) {
        return commonLogin(dto, request);
    }

    @Override
    public LoginVo emailLogin(EmailDto dto, HttpServletRequest request) {
        return commonLogin(dto, request);
    }

    @Override
    public LoginVo smsLogin(SmsDto dto, HttpServletRequest request) {
        return commonLogin(dto, request);
    }

    @Override
    public LoginVo oauth2Login(Oauth2Dto dto, HttpServletRequest request) {
        return commonLogin(dto, request);
    }

    @Override
    public void defaultOauth2LoginResponse(LoginVo loginVo, HttpServletResponse response) {
        String redirectUrl = loginProperties.getDefaultOauth2LoginCallbackUrl() + "?token=" + loginVo.getAccessToken();
        if (ObjectUtils.isNotEmpty(loginVo.getRefreshToken())) {
            redirectUrl += "&refreshToken=" + loginVo.getRefreshToken();
        }
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", redirectUrl);
    }

    @Override
    public void defaultOauth2LoginBindResponse(LoginVo loginVo, HttpServletResponse response) {
        defaultOauth2LoginResponse(loginVo, response);
    }

    @Override
    public String getUsername(String username) {
        if (username.matches(RegConstants.EMAIL_MATCHER)) {
            return queryUsername("email", username);
        } else if (username.matches(RegConstants.PHONE_MATCHER)) {
            return queryUsername("phone", username);
        } else {
            return username;
        }
    }

    private LoginVo commonLogin(LoginEntity loginBody, HttpServletRequest request) {
        LoginStrategy strategy = LoginStrategyFactory.getStrategy(loginBody.getLoginType());
        return strategy.login(loginBody, request);
    }

    private String queryUsername(String col, String val) {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq(col, val));
        if (ObjectUtils.isEmpty(sysUser)) {
            ResType type = ResType.USER_NOT_EXIST;
            throw new ServiceException(type.getCode(), MessageUtils.message(type.getI18n()));
        }
        return sysUser.getUserName();
    }
}

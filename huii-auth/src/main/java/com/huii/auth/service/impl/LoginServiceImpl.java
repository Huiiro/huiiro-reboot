package com.huii.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huii.auth.core.entity.LoginEntity;
import com.huii.auth.core.entity.dto.AccountDto;
import com.huii.auth.core.entity.dto.EmailDto;
import com.huii.auth.core.entity.dto.SmsDto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.factory.LoginStrategyFactory;
import com.huii.auth.service.LoginService;
import com.huii.auth.strategy.LoginStrategy;
import com.huii.common.constants.RegConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.system.mapper.SysUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final SysUserMapper sysUserMapper;

    @Override
    public LoginVo accountLogin(AccountDto loginBody, HttpServletRequest request) {
        return commonLogin(loginBody, request);
    }

    @Override
    public LoginVo emailLogin(EmailDto loginBody, HttpServletRequest request) {
        return commonLogin(loginBody, request);
    }

    @Override
    public LoginVo smsLogin(SmsDto loginBody, HttpServletRequest request) {
        return commonLogin(loginBody, request);
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

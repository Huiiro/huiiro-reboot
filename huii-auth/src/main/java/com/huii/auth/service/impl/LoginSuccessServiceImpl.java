package com.huii.auth.service.impl;

import com.huii.auth.config.properties.JwtProperties;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginSuccessService;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.IpAddressUtils;
import com.huii.system.mapper.SysUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginSuccessServiceImpl implements LoginSuccessService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final RedisTemplateUtils RedisTemplateUtils;

    @Override
    @SneakyThrows
    public void updateUserLoginInfo(LoginUser loginUser, HttpServletRequest request) {
        SysUser user = loginUser.getUser();
        user.setLoginIp(IpAddressUtils.getIp(request));
        user.setLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
    }

    @Override
    public LoginVo createToken(LoginUser loginUser) {
        Long id = loginUser.getUser().getUserId();
        LoginVo vo = new LoginVo();
        if ("true".equals(jwtProperties.getEnableDev())) {
            //dev token
            String token = jwtUtils.createToken(String.valueOf(id), (long) jwtProperties.getDev() * 24 * 60 * 60 * 1000);
            RedisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtProperties.getDev(), TimeUnit.DAYS);
            RedisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, token, jwtProperties.getDev(), TimeUnit.DAYS);
        } else if ("true".equals(jwtProperties.getEnableDoubleToken())) {
            //access && refresh
            String accessToken = jwtUtils.createAccessToken(id);
            String refreshToken = jwtUtils.createRefreshToken(id);
            RedisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getRefresh(), TimeUnit.HOURS);
            //是否保存accessToken至redis，默认不保存
            if (Objects.equals(jwtProperties.getSaveToken(), Boolean.TRUE.toString())) {
                RedisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, accessToken, jwtUtils.getAccess(), TimeUnit.HOURS);
            }
            RedisTemplateUtils.setCacheObject(CacheConstants.REFRESH + id, refreshToken, jwtUtils.getRefresh(), TimeUnit.HOURS);

            vo.setAccessToken(accessToken);
            vo.setRefreshToken(refreshToken);
        } else {
            //only access
            String token = jwtUtils.createSingleToken(id);
            RedisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getSingle(), TimeUnit.HOURS);
            //是否保存单token至redis，默认保存
            if (Objects.equals(jwtProperties.getSaveToken(), Boolean.TRUE.toString())) {
                RedisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, token, jwtUtils.getSingle(), TimeUnit.HOURS);
            }
            vo.setAccessToken(token);
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("userId", loginUser.getUser().getUserId());
        map.put("username", loginUser.getUser().getUserName());
        map.put("userAvatar", loginUser.getUser().getAvatar());
        vo.setUserInfo(map);
        return vo;
    }

    @Override
    public LoginVo autoCreateToken(LoginUser loginUser, HttpServletRequest request) {
        updateUserLoginInfo(loginUser, request);
        return createToken(loginUser);
    }
}

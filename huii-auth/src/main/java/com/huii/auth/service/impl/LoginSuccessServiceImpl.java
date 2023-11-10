package com.huii.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.auth.config.properties.JwtProperties;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginSuccessService;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.domain.SysUserOauth;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.LoginType;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.IpAddressUtils;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserOauthMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginSuccessServiceImpl implements LoginSuccessService {

    private final SysUserMapper sysUserMapper;
    private final SysUserOauthMapper sysUserOauthMapper;
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
        if (loginUser.getBindStatus().equals(SystemConstants.STATUS_0)) {
            vo.setNeedBind(true);
        }
        return vo;
    }

    @Override
    public LoginVo autoCreateToken(LoginUser loginUser, HttpServletRequest request) {
        updateUserLoginInfo(loginUser, request);
        return createToken(loginUser);
    }

    @Override
    public SysUserOauth getOauthUserInfo(Oauth2User oauth2User) {
        return sysUserOauthMapper.selectOne(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getOauthUserName, oauth2User.getType())
                .eq(SysUserOauth::getOauthIdentify, oauth2User.getId())
                .last("limit 1"));
    }

    @Override
    public SysUserOauth createUserOauthEntity(Long userId, Oauth2User oauth2User) {
        SysUserOauth sysUserOauth = new SysUserOauth();
        sysUserOauth.setUserId(userId);
        sysUserOauth.setOauthProvider(oauth2User.getType());
        sysUserOauth.setOauthUserName(oauth2User.getName());
        sysUserOauth.setOauthUserAvatar(oauth2User.getAvatar());
        sysUserOauth.setOauthIdentify(oauth2User.getId());
        sysUserOauthMapper.insert(sysUserOauth);
        return sysUserOauth;
    }

    @Override
    public SysUser createUserEntity(Oauth2User oauth2User) {
        SysUser sysUser = new SysUser();
        sysUser.setUserStatus("1");
        sysUser.setDeleteFlag("0");
        sysUser.setUserName(genRandomUsername());
        sysUser.setNickName(oauth2User.getName());
        sysUser.setAvatar(oauth2User.getAvatar());
        sysUserMapper.insert(sysUser);
        return sysUser;
    }

    private String genRandomUsername() {
        return "user_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}

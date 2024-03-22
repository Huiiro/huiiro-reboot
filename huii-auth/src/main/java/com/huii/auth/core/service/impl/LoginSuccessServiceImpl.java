package com.huii.auth.core.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.auth.config.properties.JwtProperties;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.core.service.LoginSuccessService;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.IpAddressUtils;
import com.huii.system.domain.SysUserOauth;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserOauthMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录成功处理服务实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class LoginSuccessServiceImpl implements LoginSuccessService {

    private final SysUserMapper sysUserMapper;
    private final SysUserOauthMapper sysUserOauthMapper;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final RedisTemplateUtils redisTemplateUtils;

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
        if (SystemConstants.TRUE.equals(jwtProperties.getEnableDev())) {
            //dev token
            String token = jwtUtils.createToken(String.valueOf(id), (long) jwtProperties.getDev() * 24 * 60 * 60 * 1000);
            redisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtProperties.getDev(), TimeUnit.DAYS);
            redisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, token, jwtProperties.getDev(), TimeUnit.DAYS);
        } else if (SystemConstants.TRUE.equals(jwtProperties.getEnableDoubleToken())) {
            //access && refresh
            String accessToken = jwtUtils.createAccessToken(id);
            String refreshToken = jwtUtils.createRefreshToken(id);
            loginUser.setRefreshToken(refreshToken);
            redisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getRefresh(), TimeUnit.HOURS);
            //是否保存accessToken至redis，默认不保存
            if (Objects.equals(jwtProperties.getSaveAccessToken(), Boolean.TRUE.toString())) {
                redisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, accessToken, jwtUtils.getAccess(), TimeUnit.HOURS);
            }
            vo.setAccessToken(accessToken);
            vo.setRefreshToken(refreshToken);
        } else {
            //only access
            //as access token,but longer than access token
            String token = jwtUtils.createSingleToken(id);
            redisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getSingle(), TimeUnit.HOURS);
            //是否保存单token至redis，默认保存
            if (Objects.equals(jwtProperties.getSaveAccessToken(), Boolean.TRUE.toString())) {
                redisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, token, jwtUtils.getSingle(), TimeUnit.HOURS);
            }
            vo.setAccessToken(token);
        }
        if (loginUser.getBindStatus().equals(SystemConstants.STATUS_0)) {
            vo.setNeedBind(true);
        }
        return loginVoBuilder(vo, loginUser);
    }

    @Override
    public int updateUserAuthsInfo(LoginUser loginUser) {
        if (ObjectUtils.isEmpty(loginUser)) {
            return 0;
        }
        int counter = 0;
        Long id = loginUser.getUser().getUserId();
        if (SystemConstants.FALSE.equals(jwtProperties.getEnableDoubleToken()) &&
                SystemConstants.TRUE.equals(jwtProperties.getSaveAccessToken())) {
            //只开启单Token时 并且开启Redis验证
            redisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getSingle(), TimeUnit.HOURS);
            counter++;
        } else if (SystemConstants.TRUE.equals(jwtProperties.getEnableDoubleToken()) &&
                SystemConstants.TRUE.equals(jwtProperties.getSaveAccessToken())) {
            //双Token时 并且开启Redis验证
            redisTemplateUtils.setCacheObject(CacheConstants.USER + id, loginUser, jwtUtils.getRefresh(), TimeUnit.HOURS);
            counter++;
        }
        return counter;
    }

    @Override
    public int updateUserAuthsInfoByUserId(List<Long> userIds, List<String> auths) {
        if (userIds.isEmpty()) {
            return 0;
        }
        int counter = 0;
        HashSet<String> newAuths = new HashSet<>(auths);
        for (Long id : userIds) {
            //从缓存加载用户信息
            JSONObject object = redisTemplateUtils.getCacheObject(CacheConstants.USER + id);
            if (ObjectUtils.isNotEmpty(object)) {
                LoginUser loginUser = object.toJavaObject(LoginUser.class);
                loginUser.setStringAuthorities(newAuths);
                counter += updateUserAuthsInfo(loginUser);
            }
        }
        return counter;
    }

    @Override
    public int updateUserRolesByUserId(List<Long> userIds, List<SysRole> roles) {
        int counter = 0;
        for (Long id : userIds) {
            JSONObject object = redisTemplateUtils.getCacheObject(CacheConstants.USER + id);
            if (ObjectUtils.isNotEmpty(object)) {
                LoginUser loginUser = object.toJavaObject(LoginUser.class);
                loginUser.getUser().setRoles(roles);
                counter += updateUserAuthsInfo(loginUser);
            }
        }
        return counter;
    }

    @Override
    public LoginVo autoCreateToken(LoginUser loginUser, HttpServletRequest request) {
        updateUserLoginInfo(loginUser, request);
        return createToken(loginUser);
    }

    @Override
    public SysUserOauth getOauthUserInfo(Oauth2User oauth2User) {
        return sysUserOauthMapper.selectOne(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getOauthProvider, oauth2User.getType())
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
        sysUserOauth.setCreateTime(LocalDateTime.now());
        sysUserOauthMapper.insert(sysUserOauth);
        return sysUserOauth;
    }

    @Override
    public SysUser createUserEntity(Oauth2User oauth2User) {
        SysUser sysUser = new SysUser();
        sysUser.setUserStatus(SystemConstants.STATUS_1);
        sysUser.setDeleteFlag(SystemConstants.STATUS_0);
        sysUser.setUserName(genRandomUsername());
        sysUser.setNickName(oauth2User.getName());
        sysUser.setAvatar(oauth2User.getAvatar());
        sysUserMapper.insert(sysUser);
        return sysUser;
    }

    @Override
    public boolean checkUserBind(Long userId, String type) {
        return sysUserOauthMapper.exists(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getUserId, userId)
                .eq(SysUserOauth::getOauthProvider, type));
    }

    @Override
    public boolean checkAccBind(Oauth2User oauth2User) {
        return sysUserOauthMapper.exists(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getOauthIdentify, oauth2User.getId())
                .eq(SysUserOauth::getOauthProvider, oauth2User.getType()));
    }

    private String genRandomUsername() {
        return "user_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }

    public static LoginVo loginVoBuilder(LoginVo loginVo, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(loginVo)) {
            loginVo = new LoginVo();
        }
        SysUser user = loginUser.getUser();
        Map<String, Object> map = new HashMap<>(3);
        map.put("userId", user.getUserId());
        map.put("username", user.getUserName());
        map.put("userAvatar", user.getAvatar());
        loginVo.setUserInfo(map);
        //set user permission status
        loginVo.setPermissions(new ArrayList<>(loginUser.getStringAuthorities()));
        //set user roles
        loginVo.setRoles(user.getRoles().stream().map(SysRole::getRoleName).collect(Collectors.toList()));

        return loginVo;
    }
}

package com.huii.auth.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginUserOauthService;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.system.domain.SysUserOauth;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserOauthMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 登录用户授权服务实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class LoginUserOauthServiceImpl implements LoginUserOauthService {

    private final SysUserMapper sysUserMapper;
    private final SysUserOauthMapper sysUserOauthMapper;
    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public List<SysUserOauth> getMyBindStatus(Long userId) {
        return sysUserOauthMapper.selectList(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getUserId, userId));
    }

    @Override
    public void cancelGrantOauth(Long userId, String provider) {
        sysUserOauthMapper.delete(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getUserId, userId)
                .eq(SysUserOauth::getOauthProvider, provider));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void bindUser(Long userId, LoginVo loginVo) {
        Long newUserId = (Long) loginVo.getUserInfo().get("userId");
        if (Objects.equals(userId, newUserId)) {
            throw new RuntimeException("无法绑定至当前帐号");
        }
        //userId 当前登录用户的ID loginVo新登录身份
        //获取旧登陆信息
        JSONObject object = redisTemplateUtils.getCacheObject(CacheConstants.USER + userId);
        if (ObjectUtils.isEmpty(object)) {
            throw new RuntimeException("当前登录信息获取失败");
        }
        LoginUser loginUser = object.toJavaObject(LoginUser.class);
        String oauthProvider = loginUser.getLoginType().getName();
        //查询旧绑定信息
        SysUserOauth userOauth = sysUserOauthMapper.selectOne(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getUserId, userId)
                .eq(SysUserOauth::getOauthProvider, oauthProvider)
                .last("limit 1"));
        if (ObjectUtils.isEmpty(userOauth)) {
            throw new RuntimeException("未查询到绑定记录");
        }
        //删除旧绑定信息
        sysUserOauthMapper.deleteById(userOauth);

        //绑定新信息
        userOauth.setUserId(newUserId);
        userOauth.setCreateTime(LocalDateTime.now());
        sysUserOauthMapper.insert(userOauth);

        //删除旧用户信息
        int deleted = sysUserMapper.realDelete(userId);
        if (deleted <= 0) {
            throw new RuntimeException("解绑失败");
        }
        //删除旧缓存信息
        redisTemplateUtils.deleteObject(CacheConstants.USER + userId);
        redisTemplateUtils.deleteObject(CacheConstants.TOKEN + userId);
    }

    @Override
    public void bindThisToMyAccount(Long userId, String provider) {
    }

}

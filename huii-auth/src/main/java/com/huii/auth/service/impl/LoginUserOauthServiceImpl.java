package com.huii.auth.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginUserOauthService;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.LoginType;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.system.domain.SysUserOauth;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserOauthMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        //获取登陆方式
        JSONObject object = redisTemplateUtils.getCacheObject(CacheConstants.USER + userId);
        if (ObjectUtils.isEmpty(object)) {
            return;
        }
        LoginUser loginUser = object.toJavaObject(LoginUser.class);
        LoginType loginType = loginUser.getLoginType();

        //查询旧信息
        SysUserOauth userOauth = sysUserOauthMapper.selectOne(new LambdaQueryWrapper<SysUserOauth>()
                .eq(SysUserOauth::getUserId, userId)
                .eq(SysUserOauth::getOauthProvider, loginType.getName()));
        sysUserOauthMapper.deleteById(userOauth);
        //获取需要绑定的ID
        Long bindToId = (Long) loginVo.getUserInfo().get("userId");
        userOauth.setUserId(bindToId);
        //更新绑定信息
        sysUserOauthMapper.updateById(userOauth);

        //删除旧用户信息
        sysUserMapper.deleteById(userId);
        //删除旧缓存信息
        redisTemplateUtils.deleteObject(CacheConstants.USER + userId);
        redisTemplateUtils.deleteObject(CacheConstants.TOKEN + userId);
    }

    @Override
    public void bindThisToMyAccount(Long userId, String provider) {
    }

}

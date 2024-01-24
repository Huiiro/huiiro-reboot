package com.huii.auth.service;

import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.system.domain.SysUserOauth;

import java.util.List;

/**
 * 登录用户授权服务
 *
 * @author huii
 */
public interface LoginUserOauthService {


    /**
     * 查询授权情况
     *
     * @param userId userId
     * @return list
     */
    List<SysUserOauth> getMyBindStatus(Long userId);

    /**
     * 用户取消授权
     *
     * @param userId   userId
     * @param provider provider
     */
    void cancelGrantOauth(Long userId, String provider);

    /**
     * 绑定已有帐号
     *
     * @param userId  当前登录账号（第三方登录方式）
     * @param loginVo 新登录账号（绑定的账号）
     */
    void bindUser(Long userId, LoginVo loginVo);


    /**
     * 绑定第三方账号信息至本账号
     *
     * @param userId   userId
     * @param provider provider
     */
    void bindThisToMyAccount(Long userId, String provider);

}

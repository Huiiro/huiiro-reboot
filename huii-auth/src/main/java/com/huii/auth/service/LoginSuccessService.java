package com.huii.auth.service;

import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.system.domain.SysUserOauth;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 登录成功处理服务
 *
 * @author huii
 */
public interface LoginSuccessService {

    /**
     * 更新用户登录信息
     *
     * @param loginUser loginUser
     * @param request   request
     */
    void updateUserLoginInfo(LoginUser loginUser, HttpServletRequest request);

    /**
     * 根据授权对象颁发 token
     *
     * @param loginUser loginUser
     * @return token...
     */
    LoginVo createToken(LoginUser loginUser);

    /**
     * 更新用户权限信息后处理 redis 信息
     *
     * @param loginUser loginUser
     * @return counter
     */
    int updateUserAuthsInfo(LoginUser loginUser);

    /**
     * 更新用户权限信息后处理 redis 信息
     *
     * @param userIds userIds
     * @param auths   auths
     * @return counter
     */
    int updateUserAuthsInfoByUserId(List<Long> userIds, List<String> auths);

    /**
     * 更新角色授权后处理 redis 信息
     *
     * @param userIds userIds
     * @param roles   roles
     * @return counter
     */
    int updateUserRolesByUserId(List<Long> userIds, List<SysRole> roles);

    /**
     * 自动登录方法 包含更新用户信息和授权
     *
     * @param loginUser loginUser
     * @param request   request
     * @return token...
     */
    LoginVo autoCreateToken(LoginUser loginUser, HttpServletRequest request);

    /**
     * 查询用户 oauth 登录信息
     *
     * @param oauth2User oauth2User
     * @return SysUserOauth
     */
    SysUserOauth getOauthUserInfo(Oauth2User oauth2User);

    /**
     * 创建 SysUserOauth
     *
     * @param userId     userId
     * @param oauth2User oauth2User
     * @return SysUserOauth
     */
    SysUserOauth createUserOauthEntity(Long userId, Oauth2User oauth2User);

    /**
     * 创建 SysUser
     *
     * @param oauth2User oauth2User
     * @return SysUser
     */
    SysUser createUserEntity(Oauth2User oauth2User);

    /**
     * 检查用户是否绑定该账号
     *
     * @param userId userId
     * @param type   type
     */
    boolean checkUserBind(Long userId, String type);

    /**
     * 检查该账号是否已被绑定
     *
     * @param oauth2User oauth2User
     * @return b
     */
    boolean checkAccBind(Oauth2User oauth2User);
}

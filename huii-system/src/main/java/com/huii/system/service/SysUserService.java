package com.huii.system.service;

import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;
import java.util.Map;

public interface SysUserService {

    /**
     * 查询全部用户
     *
     * @param sysUser sysUser
     * @return list
     */
    List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 获取用户列表
     *
     * @param sysUser   sysUser
     * @param pageParam pageParam
     * @return page
     */
    Page selectUserList(SysUser sysUser, PageParam pageParam);

    /**
     * 获取用户
     *
     * @param id id
     * @return sysUser
     */
    SysUser selectUserById(Long id);

    /**
     * 校验添加用户
     *
     * @param sysUser sysUser
     */
    void checkInsert(SysUser sysUser);

    /**
     * 添加用户
     *
     * @param sysUser sysUser
     */
    void insertUser(SysUser sysUser);

    /**
     * 校验更新用户
     *
     * @param sysUser sysUser
     */
    void checkUpdate(SysUser sysUser);

    /**
     * 更新用户
     *
     * @param sysUser sysUser
     */
    void updateUser(SysUser sysUser);

    /**
     * 更新用户密码
     *
     * @param sysUser sysUser
     */
    void updateUserPassword(SysUser sysUser);

    /**
     * 更新用户密码
     *
     * @param userId id
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    void updateUserPassword(Long userId, String oldPwd, String newPwd);

    /**
     * 更新用户密码
     *
     * @param identify 身份
     * @param column   身份对应字段
     * @param pwd      新密码
     */
    void updateUserPassword(String identify, String column, String pwd);

    /**
     * 更新用户资料
     *
     * @param sysUser sysUser
     */
    void updateUserProfile(SysUser sysUser);

    /**
     * 更新用户头像
     *
     * @param userId userId
     * @param url    url
     * @return old avatar url
     */
    String updateUserAvatar(Long userId, String url);

    /**
     * 删除用户
     *
     * @param ids ids
     */
    void deleteUsers(Long[] ids);

    /**
     * 查询未分配用户
     * <p>role service</p>
     *
     * @param sysUser   sysUser
     * @param pageParam pageParam
     * @return page
     */
    Page queryNonAuthUser(SysUser sysUser, PageParam pageParam);

    /**
     * 查询已分配用户
     * <p>role service</p>
     *
     * @param sysUser   sysUser
     * @param pageParam pageParam
     * @return page
     */
    Page queryAuthUser(SysUser sysUser, PageParam pageParam);

    /**
     * 授权用户
     * <p>role service</p>
     *
     * @param roleId  roleId
     * @param userIds userIds
     */
    void authUser(Long roleId, Long[] userIds);

    /**
     * 取消授权用户
     * <p>role service</p>
     *
     * @param roleId  roleId
     * @param userIds userIds
     */
    void unauthUser(Long roleId, Long[] userIds);

    /**
     * 查询订阅/未订阅用户
     *
     * @param sysUser   sysUser
     * @param pageParam pageParam
     * @param userIds   userIds
     * @return page
     */
    Page queryUserSubscribe(SysUser sysUser, PageParam pageParam, List<Long> userIds);

    /**
     * 查询用户绑定的手机和邮箱
     */
    Map<String, String> queryUserBindPhoneOrEmail();

    /**
     * 校验本人邮箱
     *
     * @param email  email
     * @param userId userId
     */
    void checkUserEmail(String email, Long userId);

    /**
     * 绑定邮箱
     *
     * @param uid   uid
     * @param email email
     */
    void bindEmail(Long uid, String email);


}

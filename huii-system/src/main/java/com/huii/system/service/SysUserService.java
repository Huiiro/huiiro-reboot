package com.huii.system.service;

import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysUserService {

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
     * @param sysUser sysUser
     */
    void updateUserPassword(SysUser sysUser);

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

}

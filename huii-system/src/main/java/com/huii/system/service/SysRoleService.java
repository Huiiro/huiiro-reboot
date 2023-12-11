package com.huii.system.service;

import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

import java.util.List;

public interface SysRoleService {

    /**
     * 获取角色列表
     *
     * @param sysRole   role
     * @param pageParam pageParam
     * @return page
     */
    Page selectRoleList(SysRole sysRole, PageParam pageParam);

    /**
     * 获取全部角色
     *
     * @return list
     */
    List<Label> selectRolesAll();

    /**
     * 获取用户已分配的角色ID
     *
     * @return list
     */
    List<Long> selectUserRoleIds();

    /**
     * 获取角色
     *
     * @param id id
     * @return role
     */
    SysRole selectRoleById(Long id);

    /**
     * 检查添加数据是否合法
     *
     * @param sysRole sysRole
     */
    void checkInsert(SysRole sysRole);

    /**
     * 添加角色
     *
     * @param sysRole sysRole
     */
    void insertRole(SysRole sysRole);

    /**
     * 检查修改数据是否合法
     *
     * @param sysRole sysRole
     */
    void checkUpdate(SysRole sysRole);

    /**
     * 更新角色
     *
     * @param sysRole sysRole
     */
    void updateRole(SysRole sysRole);

    /**
     * 更新角色状态
     *
     * @param sysRole sysRole
     */
    void updateRoleStatus(SysRole sysRole);


    /**
     * 更新角色权限
     *
     * @param sysRole sysRole
     */
    void updateRoleAuths(SysRole sysRole);

    /**
     * 更新角色数据权限
     *
     * @param sysRole sysRole
     */
    void updateRoleDataScope(SysRole sysRole);

    /**
     * 删除角色
     *
     * @param ids ids
     */
    void deleteRoles(Long[] ids);
}

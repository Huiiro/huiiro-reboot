package com.huii.system.service;

import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;

public interface SysRoleService {

    /**
     * 获取分页查询角色信息
     *
     * @param sysRole      role
     * @param pageParam pageParam
     * @return page
     */
    Page selectRoleList(SysRole sysRole, PageParam pageParam);

    /**
     * 根据id查询角色信息
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
     * 修改角色
     *
     * @param sysRole sysRole
     */
    void updateRole(SysRole sysRole);

    /**
     * 修改角色状态
     * @param sysRole sysRole
     */
    void updateRoleStatus(SysRole sysRole);

    /**
     * 删除角色
     *
     * @param ids ids
     */
    void deleteRoles(Long[] ids);
}

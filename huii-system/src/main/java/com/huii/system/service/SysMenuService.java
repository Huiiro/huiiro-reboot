package com.huii.system.service;

import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.model.Route;
import com.huii.common.core.model.Tree;

import java.util.List;

public interface SysMenuService {
    /**
     * 查询菜单list
     *
     * @param sysMenu sysMenu
     * @param userId  userId
     * @return list
     */
    List<SysMenu> selectMenuList(SysMenu sysMenu, Long userId);

    /**
     * 构建前端所需route
     * @param list list
     * @return routes
     */
    List<Route> buildRoutes(List<SysMenu> list);

    /**
     * 查询菜单treeList
     * @param menus menu list
     * @return tree list
     */
    List<SysMenu> buildTree(List<SysMenu> menus);

    /**
     * 构建下拉树
     * @param menus menus
     * @param addHeadNode addHeadNode
     * @return tree
     */
    List<Tree> buildSelect(List<SysMenu> menus, Boolean addHeadNode);

    /**
     * 查询角色已经分配的菜单id
     * @param roleId roleId
     * @return ids
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 查询菜单
     *
     * @param id id
     * @return menu
     */
    SysMenu selectMenuById(Long id);

    /**
     * 检查添加数据是否合法
     * @param sysMenu sysMenu
     */
    void checkInsert(SysMenu sysMenu);

    /**
     * 添加菜单
     *
     * @param sysMenu sysMenu
     */
    void insertMenu(SysMenu sysMenu);

    /**
     * 检查修改的数据是否合法
     * @param sysMenu sysMenu
     */
    void checkUpdate(SysMenu sysMenu);

    /**
     * 更新菜单
     *
     * @param sysMenu sysMenu
     */
    void updateMenu(SysMenu sysMenu);

    /**
     * 删除菜单
     * @param allow 是否允许删除子菜单
     * @param id id
     */
    void deleteMenu(String allow, Long id);
}

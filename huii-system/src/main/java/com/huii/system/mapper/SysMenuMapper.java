package com.huii.system.mapper;

import com.huii.common.core.domain.SysMenu;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapperPlus<SysMenu> {

    /**
     * 根据用户ID和查询参数查询菜单
     *
     * @param userId userId
     * @return menus
     */
    List<SysMenu> selectListMenusByUserId(@Param("menu") SysMenu sysMenu, @Param("userId") Long userId);

    /**
     * 根据角色id查询角色已分配的菜单id
     *
     * @param roleId roleId
     * @return list
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 检查是否有角色分配了传入的菜单id
     *
     * @param ids ids
     * @return boolean
     */
    boolean checkRoleHasAnyMenu(List<Long> ids);
}

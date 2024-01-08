package com.huii.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huii.common.annotation.DataScope;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapperPlus<SysUser> {

    /**
     * 执行正真删除 而不是逻辑删除
     *
     * @param userId userId
     * @return count
     */
    int realDelete(Long userId);

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
     * @param url    avatar url
     */
    void updateUserAvatar(@Param("userId") Long userId, @Param("url") String url);

    /**
     * 根据用户ID查询用户权限字段
     *
     * @param userId userId
     * @return auths list
     */
    List<String> selectAuthsByUserId(Long userId);

    /**
     * 根据角色ID查询对应角色下的权限字段
     *
     * @param roleId roleId
     * @return auths list
     */
    List<String> selectAuthsByRoleId(Long roleId);

    /**
     * 根据角色ID查询对应角色下的用户
     *
     * @param roleId roleId
     */
    List<SysUser> selectUsersByRoleId(Long roleId);

    /**
     * 根据菜单ID查询分配该菜单的用户ID
     *
     * @param menuId menuId
     * @return userIds
     */
    List<Long> selectUserIdsByMenuId(Long menuId);

    /**
     * 获取登录用户信息
     *
     * @param col col
     * @param val val
     * @return sysUserVo
     */
    SysUser selectLoginUser(String col, Object val);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId id
     * @return sysUserVo
     */
    SysUser selectUserVoById(Long userId);

    /**
     * 根据用户名称查询用户
     *
     * @param username username
     * @return sysUser
     */
    SysUser selectUserByUserName(String username);

    /**
     * 分页查询用户信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    @DataScope
    Page<SysUser> selectUserPageWithDept(@Param("page") Page<SysUser> page,
                                         @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 查询用户信息
     *
     * @param queryWrapper queryWrapper
     * @return list
     */
    @DataScope
    List<SysUser> selectUserListWithDept(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);
}

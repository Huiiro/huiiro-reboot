package com.huii.system.mapper;

import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.base.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapperPlus<SysUser> {

    /**
     * 根据用户ID查询用户权限字段
     *
     * @param userId userId
     * @return auths list
     */
    List<String> selectAuthsByUserId(Long userId);


    /**
     * 根据角色ID查询该角色对应的权限字段
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
     * 根据用户ID查询用户信息
     * @param userId id
     * @return sysUser
     */
    SysUser selectUserVoById(Long userId);
}

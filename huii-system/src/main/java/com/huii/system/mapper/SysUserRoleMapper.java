package com.huii.system.mapper;

import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole> {

    /**
     * 根据角色ID查询分配的用户ID
     *
     * @param roleId roleId
     * @return userIds
     */
    List<Long> selectUserIdsByRoleId(Long roleId);
}

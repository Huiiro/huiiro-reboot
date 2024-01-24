package com.huii.auth.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huii.auth.service.LoginSuccessService;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.system.domain.SysUserRole;
import com.huii.system.event.SecurityContextUpdateEvent;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色更新事件监听器
 *
 * @author huii
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleUpdatedEventListener {

    private final LoginSuccessService loginSuccessService;
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    @EventListener
    public void handleRoleUpdatedEvent(SecurityContextUpdateEvent event) {
        Long roleId = event.getRoleId();

        List<String> auths = event.getAuths();
        List<Long> userIds = event.getUserIds();

        if (ObjectUtils.isNotEmpty(roleId)) {
            //更新角色权限字段事件
            if (!CollectionUtils.isEmpty(auths)) {
                updateWithRoleIdAndAuths(roleId, auths);
            }
            //授权用户角色事件
            if (!CollectionUtils.isEmpty(userIds)) {
                updateWithRoleIdAndUserIds(roleId, userIds);
            }
        }
        //更新菜单事件
        Long menuId = event.getMenuId();
        if (ObjectUtils.isNotEmpty(menuId)) {
            if (!CollectionUtils.isEmpty(userIds)) {
                updateWithMenuIdAndUserIds(menuId, userIds);
            }
        }
    }


    /**
     * 更新角色权限字段事件 更新 LoginUser stringAuthorities 字段
     */
    public void updateWithRoleIdAndAuths(Long roleId, List<String> auths) {
        //获取需要更新的用户ID
        List<SysUserRole> list = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId));
        List<Long> userIds = list.stream().map(SysUserRole::getUserId).toList();
        int updated = loginSuccessService.updateUserAuthsInfoByUserId(userIds, auths);
        log.info("updateWithRoleIdAndAuths: update users: {}", updated);

    }

    /**
     * 授权用户角色事件 更新 LoginUser.user roles 字段
     */
    public void updateWithRoleIdAndUserIds(Long roleId, List<Long> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        SysUser sysUser = sysUserMapper.selectLoginUser("user_id", userIds.get(0));
        List<SysRole> roles = sysUser.getRoles();
        int updated = loginSuccessService.updateUserRolesByUserId(userIds, roles);
        log.info("updateWithRoleIdAndUserIds: update users: {}", updated);
    }

    public void updateWithMenuIdAndUserIds(Long menuId, List<Long> userIds) {

    }
}

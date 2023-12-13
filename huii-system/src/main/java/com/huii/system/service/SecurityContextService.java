package com.huii.system.service;

import com.huii.system.event.SecurityContextUpdateEvent;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * SecurityContextService
 *
 * @author huii
 * @see com.huii.auth.listener.RoleUpdatedEventListener
 */
@Service
@RequiredArgsConstructor
public class SecurityContextService {

    private final SysUserMapper sysUserMapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 更新角色权限字段事件
     *
     * @param roleId roleId
     */
    public void clearUpdateAuthByRoleId(Long roleId) {
        //获取更新后的字段
        List<String> auths = sysUserMapper.selectAuthsByRoleId(roleId);
        //发布更新角色权限字段事件
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, roleId, null, auths, null);
        eventPublisher.publishEvent(event);
    }

    /**
     * 授权用户角色事件
     *
     * @param roleId  roleId 角色ID
     * @param userIds userIds 被授权用户ID
     */
    public void clearGrantAuthByRoleId(Long roleId, Long[] userIds) {
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, roleId, null, null, Arrays.asList(userIds));
        eventPublisher.publishEvent(event);
    }

    /**
     * 更新菜单事件
     *
     * @param menuId menuId
     */
    public void clearUpdateMenuByMenuId(Long menuId) {
        //获取需要更新的用户
        List<Long> userIds = sysUserMapper.selectUserIdsByMenuId(menuId);
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, null, menuId, null, userIds);
        eventPublisher.publishEvent(event);
    }
}

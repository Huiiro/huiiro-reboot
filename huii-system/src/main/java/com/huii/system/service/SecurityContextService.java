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

    public void clearUpdateAuthByRoleId(Long roleId) {
        List<String> auths = sysUserMapper.selectAuthsByRoleId(roleId);
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, roleId, null, auths, null);
        eventPublisher.publishEvent(event);
    }

    public void clearGrantAuthByRoleId(Long roleId, Long[] userIds) {
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, roleId, null, null, Arrays.asList(userIds));
        eventPublisher.publishEvent(event);
    }

    public void clearUpdateMenuByMenuId(Long menuId) {
        List<Long> list = sysUserMapper.selectUserIdsByMenuId(menuId);
        SecurityContextUpdateEvent event = new SecurityContextUpdateEvent(this, null, menuId, null, list);
        eventPublisher.publishEvent(event);
    }
}

package com.huii.auth.listener;

import com.huii.auth.service.LoginSuccessService;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.system.event.SecurityContextUpdateEvent;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleUpdatedEventListener {

    private final LoginSuccessService loginSuccessService;
    private final SysUserMapper sysUserMapper;

    @EventListener
    public void handleRoleUpdatedEvent(SecurityContextUpdateEvent event) {
        Long roleId = event.getRoleId();

        List<String> menuIds = event.getAuths();
        List<Long> userIds = event.getUserIds();

        if (ObjectUtils.isNotEmpty(roleId)) {

            if (!CollectionUtils.isEmpty(menuIds)) {
                updateWithRoleIdAndAuths(roleId, menuIds);
            }

            if (!CollectionUtils.isEmpty(userIds)) {
                updateWithRoleIdAndUserIds(roleId, userIds);
            }
        }

        Long menuId = event.getMenuId();
        if (ObjectUtils.isNotEmpty(menuId)) {
            if (!CollectionUtils.isEmpty(userIds)) {
                updateWithMenuIdAndUserIds(menuId, userIds);
            }
        }
    }


    /**
     * 更新权限 只更新用户信息的权限字段
     */
    public void updateWithRoleIdAndAuths(Long roleId, List<String> auth) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        boolean matches = Optional.ofNullable(loginUser.getUser().getRoles())
                .orElse(Collections.emptyList())
                .stream()
                .map(SysRole::getRoleId)
                .anyMatch(r -> Objects.equals(r, roleId));

        if (matches) {
            loginUser.setStringAuthorities(new HashSet<>(auth));
            Authentication newAuthentication = new
                    UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            securityContext.setAuthentication(newAuthentication);
            loginSuccessService.updateUserAuthsInfo(loginUser);
            log.info("user: {} , updated auth!", loginUser.getUser().getUserName());
        }
    }

    /**
     * 分配角色 只更新用户信息的角色信息
     */
    public void updateWithRoleIdAndUserIds(Long roleId, List<Long> userIds) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        Long userId = loginUser.getUser().getUserId();
        if (userIds.contains(userId)) {
            SysUser sysUser = sysUserMapper.selectLoginUser("user_id", userId);
            loginUser.setUser(sysUser);
            Authentication newAuthentication = new
                    UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            securityContext.setAuthentication(newAuthentication);
            loginSuccessService.updateUserAuthsInfo(loginUser);
            log.info("user: {} , updated user!", loginUser.getUser().getUserName());
        }
    }

    public void updateWithMenuIdAndUserIds(Long menuId, List<Long> userIds) {
        updateWithRoleIdAndUserIds(null, userIds);
    }
}

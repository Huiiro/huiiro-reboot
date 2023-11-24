package com.huii.auth.listener;

import com.huii.auth.service.LoginSuccessService;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.LoginUser;
import com.huii.system.event.RoleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleUpdatedEventListener {

    private final LoginSuccessService loginSuccessService;

    @EventListener
    public void handleRoleUpdatedEvent(RoleUpdatedEvent event) {
        List<String> menuIds = event.getAuths();
        Long roleId = event.getRoleId();
        updateUserAuths(roleId, menuIds);
    }

    public void updateUserAuths(Long roleId, List<String> auth) {

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
}

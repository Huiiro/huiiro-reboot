package com.huii.framework.core.service;

import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * spring security 权限校验自定义实现
 *
 * @author huii
 */
@Service("ap")
public class AuthenticationPermissionService {

    /**
     * 用户是否具有某一权限
     */
    public boolean hasAuth(String auth) {
        if (StringUtils.isEmpty(auth)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getPrincipal();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getAuthorities())) {
            return false;
        }
        if (checkIsAdminPermission(loginUser)) {
            return true;
        }
        return loginUser.getStringAuthorities().contains(auth);
    }

    /**
     * 用户是否具有任一权限
     */
    public boolean hasAnyAuths(String auths) {
        if (StringUtils.isEmpty(auths)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getPrincipal();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getAuthorities())) {
            return false;
        }
        if (checkIsAdminPermission(loginUser)) {
            return true;
        }
        for (String auth : auths.split(",")) {
            if (loginUser.getStringAuthorities().contains(auth)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户是否具有某一角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getPrincipal();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        if (checkIsAdminPermission(loginUser)) {
            return true;
        }
        return loginUser.getUser().getRoles().stream().map(SysRole::getRoleName).anyMatch(
                roleName -> roleName.equals(role));
    }

    /**
     * 用户是否具有任一角色
     */
    public boolean hasAnyRole(String roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getPrincipal();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        if (checkIsAdminPermission(loginUser)) {
            return true;
        }
        List<String> userRoles = loginUser.getUser().getRoles().stream().map(SysRole::getRoleName).toList();
        for (String role : roles.split(",")) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIsAdminPermission(LoginUser loginUser) {
        return loginUser.getUser().getUserId().equals(SystemConstants.ADMIN_ID) &&
                loginUser.getStringAuthorities().contains(SystemConstants.ALL_PERMISSION);
    }
}

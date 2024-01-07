package com.huii.common.utils;

import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.common.exception.BasicAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

/**
 * security工具
 *
 * @author huii
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getPrincipal().getUser().getUserId();
    }

    /**
     * 安全获取用户ID 不会抛出异常，返回null
     */
    public static Long safeGetUserId() {
        try {
            return getPrincipal().getUser().getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return getPrincipal().getUsername();
    }

    /**
     * 获取用户信息
     */
    public static SysUser getUser() {
        return getPrincipal().getUser();
    }

    /**
     * 获取用户权限字段
     */
    public static Set<String> getUserAuths() {
        return getPrincipal().getStringAuthorities();
    }

    /**
     * 获取 Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取 Principle
     */
    public static LoginUser getPrincipal() {
        Authentication authentication = getAuthentication();
        if (authentication.getPrincipal() instanceof String str) {
            if (str.equals("anonymousUser")) {
                throw new BasicAuthenticationException("获取登录信息失败，请重新登录");
            }
        }
        return (LoginUser) authentication.getPrincipal();
    }

    /**
     * 加密密码
     */
    public static String encryptPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return password;
        }
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * 比较密码
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public static boolean isAdmin() {
        try {
            Long userId = getUserId();
            return SystemConstants.ADMIN_ID.equals(userId);
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isAdmin(Long id) {
        return SystemConstants.ADMIN_ID.equals(id);
    }

    public static boolean isRoleAdmin(Long... ids) {
        for (Long id : ids) {
            if (SystemConstants.ADMIN_ID.equals(id)) {
                return true;
            }
        }
        return false;
    }
}

package com.huii.framework.core.service;

import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("ap")
public class AuthenticationPermissionService {

    public boolean hasAuth(String auth) {
        if (StringUtils.isEmpty(auth)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getPrincipal();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getAuthorities())) {
            return false;
        }
        return loginUser.getStringAuthorities().contains(auth);
    }

    public boolean hasRole(String... role) {
        //TODO
        return true;
    }
}

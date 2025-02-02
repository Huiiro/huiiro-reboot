package com.huii.auth.core.service.impl;

import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.ResType;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * springSecurity userDetail 实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return loadUser("user_name", username);
    }

    public UserDetails loadUserBySms(String sms) {
        return loadUser("phone", sms);
    }

    public UserDetails loadUserByEmail(String email) {
        return loadUser("email", email);
    }

    public UserDetails loadUserByUserId(Long id) {
        return loadUser("user_id", id);
    }

    public UserDetails loadUser(String col, Object val) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectLoginUser(col, val);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException(ResType.getI18nMessage(ResType.USER_NOT_EXIST));
        }
        List<String> auths = new ArrayList<>();
        if (sysUser.getUserId().equals(SystemConstants.ADMIN_ID)) {
            auths.add(SystemConstants.ALL_PERMISSION);
        } else {
            auths = sysUserMapper.selectAuthsByUserId(sysUser.getUserId());
        }
        return new LoginUser(sysUser, new HashSet<>(auths));
    }
}

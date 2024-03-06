package com.huii.auth.service.impl;

import com.huii.auth.service.ForgetPwdService;
import com.huii.common.core.domain.SysUser;
import com.huii.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgetPwdServiceImpl implements ForgetPwdService {

    private final SysUserMapper sysUserMapper;
    @Override
    public SysUser getUser(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }
}

package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysUser;
import com.huii.system.mapper.SysUserMapper;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;

}

package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysUserRole;
import com.huii.system.mapper.SysUserRoleMapper;
import com.huii.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}

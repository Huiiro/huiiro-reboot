package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysUserPost;
import com.huii.system.mapper.SysUserPostMapper;
import com.huii.system.service.SysUserPostService;
import org.springframework.stereotype.Service;

@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {
}

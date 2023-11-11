package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysLogLogin;
import com.huii.system.mapper.SysLogLoginMapper;
import com.huii.system.service.SysLogLoginService;
import org.springframework.stereotype.Service;

@Service
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements SysLogLoginService {
}

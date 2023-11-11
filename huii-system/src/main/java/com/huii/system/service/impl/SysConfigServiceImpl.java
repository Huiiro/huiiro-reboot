package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysConfig;
import com.huii.system.mapper.SysConfigMapper;
import com.huii.system.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl  extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
}

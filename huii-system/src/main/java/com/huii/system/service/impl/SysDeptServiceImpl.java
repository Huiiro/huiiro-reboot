package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysDept;
import com.huii.system.mapper.SysDeptMapper;
import com.huii.system.service.SysDeptService;
import org.springframework.stereotype.Service;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
}

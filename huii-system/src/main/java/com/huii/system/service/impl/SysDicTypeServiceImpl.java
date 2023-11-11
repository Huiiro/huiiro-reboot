package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.domain.SysDicType;
import com.huii.system.mapper.SysDicTypeMapper;
import com.huii.system.service.SysDicTypeService;
import org.springframework.stereotype.Service;

@Service
public class SysDicTypeServiceImpl  extends ServiceImpl<SysDicTypeMapper, SysDicType> implements SysDicTypeService {
}

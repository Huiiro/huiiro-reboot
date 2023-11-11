package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysLogOp;
import com.huii.system.mapper.SysLogOpMapper;
import com.huii.system.service.SysLogOpService;
import org.springframework.stereotype.Service;

@Service
public class SysLogOpServiceImpl extends ServiceImpl<SysLogOpMapper, SysLogOp> implements SysLogOpService {
}

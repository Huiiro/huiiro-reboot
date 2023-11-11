package com.huii.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.system.domain.SysFile;
import com.huii.system.mapper.SysFileMapper;
import com.huii.system.service.SysFileService;
import org.springframework.stereotype.Service;

@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {
}

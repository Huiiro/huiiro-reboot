package com.huii.generator.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.generator.entity.GenColumn;
import com.huii.generator.mapper.GenColumnMapper;
import com.huii.generator.service.GenColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenColumnServiceImpl extends ServiceImpl<GenColumnMapper, GenColumn> implements GenColumnService {

    private final GenColumnMapper genColumnMapper;
}

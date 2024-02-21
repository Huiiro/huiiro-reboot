package com.huii.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.message.domain.MsgSubscribeUser;
import com.huii.message.mapper.MsgSubscribeUserMapper;
import com.huii.message.service.MsgSubscribeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MsgSubscribeUserServiceImpl extends ServiceImpl<MsgSubscribeUserMapper, MsgSubscribeUser>
        implements MsgSubscribeUserService {

}

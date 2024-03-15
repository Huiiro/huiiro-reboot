package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.system.domain.SysMessage;
import com.huii.system.domain.dto.SysMessageDto;
import com.huii.system.domain.vo.SysMessageVo;
import com.huii.system.mapper.SysMessageMapper;
import com.huii.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    private final SysMessageMapper sysMessageMapper;

    @Override
    public Long getUnReadMessageCount(Long id) {
        return sysMessageMapper.selectCount(new LambdaQueryWrapper<SysMessage>()
                .eq(SysMessage::getReceiveId, id)
                .eq(SysMessage::getMessageRead, SystemConstants.STATUS_0));
    }

    @Override
    public com.huii.common.core.model.Page selectMessageList(SysMessage sysMessage, PageParam pageParam) {
        IPage<SysMessage> iPage = new PageParamUtils<SysMessage>().getPageInfo(pageParam);
        return new com.huii.common.core.model.Page(this.page(iPage, wrapperBuilder(sysMessage)));
    }

    @Override
    public com.huii.common.core.model.Page selectMyMessageList(SysMessage sysMessage, PageParam pageParam) {
        Page<SysMessage> page = new PageParamUtils<SysMessage>().getPage(pageParam);
        Page<SysMessageVo> voPage = sysMessageMapper.selectMyMessageList(page, wrapperBuilder(sysMessage));
        voPage.getRecords().forEach(i -> {
            if (i.getMessageRead().equals(SystemConstants.STATUS_0)) {
                SysMessage message = new SysMessage();
                message.setMessageId(i.getMessageId());
                message.setMessageRead(SystemConstants.STATUS_1);
                sysMessageMapper.updateById(message);
            }
        });
        return new com.huii.common.core.model.Page(voPage);
    }

    @Override
    public SysMessage selectMessageById(Long id) {
        return sysMessageMapper.selectById(id);
    }

    @Override
    public void checkInsert(SysMessageDto sysMessage) {

    }

    @Override
    public void insertMessage(SysMessageDto dto) {
        String[] split = dto.getReceiveId().split(",");
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessage(dto.getMessage());
        sysMessage.setMessageType(dto.getMessageType());
        sysMessage.setMessageRead(SystemConstants.STATUS_0);
        sysMessage.setMessageStatus(SystemConstants.STATUS_1);
        for (String receive : split) {
            sysMessage.setReceiveId(Long.valueOf(receive));
            sysMessageMapper.insert(sysMessage);
        }
    }

    @Override
    public void insertMessage(SysMessage sysMessage) {
        //default message type using magic 1
        sysMessage.setMessageType("1");
        sysMessage.setMessageRead(SystemConstants.STATUS_0);
        sysMessage.setMessageStatus(SystemConstants.STATUS_1);
        sysMessageMapper.insert(sysMessage);
    }

    @Override
    public void checkUpdate(SysMessage sysMessage) {

    }

    @Override
    public void updateMessage(SysMessage sysMessage) {
        sysMessageMapper.updateById(sysMessage);
    }

    @Override
    public void deleteMessage(Long[] ids) {
        sysMessageMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<SysMessage> wrapperBuilder(SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ObjectUtils.isNotEmpty(sysMessage.getMessage()), SysMessage::getMessage, sysMessage.getMessage());
        wrapper.eq(ObjectUtils.isNotEmpty(sysMessage.getReceiveId()), SysMessage::getReceiveId, sysMessage.getReceiveId());
        wrapper.eq(ObjectUtils.isNotEmpty(sysMessage.getMessageType()), SysMessage::getMessageType, sysMessage.getMessageType());
        wrapper.eq(ObjectUtils.isNotEmpty(sysMessage.getMessageRead()), SysMessage::getMessageRead, sysMessage.getMessageRead());
        wrapper.eq(ObjectUtils.isNotEmpty(sysMessage.getMessageStatus()), SysMessage::getMessageStatus, sysMessage.getMessageStatus());
        wrapper.orderByAsc(SysMessage::getCreateTime);
        return wrapper;
    }
}

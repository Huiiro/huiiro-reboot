package com.huii.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.system.domain.SysMessage;
import com.huii.system.domain.vo.SysMessageVo;
import com.huii.system.mapper.SysMessageMapper;
import com.huii.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
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
    public void checkInsert(SysMessage sysMessage) {

    }

    @Override
    public void insertMessage(SysMessage sysMessage) {
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
        wrapper.orderByAsc(SysMessage::getCreateTime);
        return wrapper;
    }
}

package com.huii.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.common.utils.TimeUtils;
import com.huii.message.domain.MsgMailTemplate;
import com.huii.message.mapper.MsgMailTemplateMapper;
import com.huii.message.service.MsgMailTemplateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MsgMailTemplateServiceImpl extends ServiceImpl<MsgMailTemplateMapper, MsgMailTemplate> implements MsgMailTemplateService {

    private final MsgMailTemplateMapper msgMailTemplateMapper;

    @Override
    public Page selectMsgMailTemplateList(MsgMailTemplate msgMailTemplate, PageParam pageParam) {
        IPage<MsgMailTemplate> iPage = new PageParamUtils<MsgMailTemplate>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(msgMailTemplate)));
    }

    @Override
    public List<Label> getLabelList() {
        return msgMailTemplateMapper.selectLabelList();
    }

    @Override
    public MsgMailTemplate selectMsgMailTemplateById(Long id) {
        return msgMailTemplateMapper.selectById(id);
    }

    @Override
    public void checkInsert(MsgMailTemplate msgMailTemplate) {
        if (msgMailTemplateMapper.exists(new LambdaQueryWrapper<MsgMailTemplate>()
                .eq(MsgMailTemplate::getTempName, msgMailTemplate.getTempName()))) {
            throw new RuntimeException("模板名称重复");
        }
    }

    @Override
    public void insertMsgMailTemplate(MsgMailTemplate msgMailTemplate) {
        msgMailTemplateMapper.insert(msgMailTemplate);
    }

    @Override
    public void checkUpdate(MsgMailTemplate msgMailTemplate) {
        MsgMailTemplate oldOne = msgMailTemplateMapper.selectById(msgMailTemplate.getMailTempId());
        if (!StringUtils.equals(msgMailTemplate.getTempName(), oldOne.getTempName())) {
            if (msgMailTemplateMapper.exists(new LambdaQueryWrapper<MsgMailTemplate>()
                    .eq(MsgMailTemplate::getTempName, msgMailTemplate.getTempName()))) {
                throw new RuntimeException("模板名称重复");
            }
        }
    }

    @Override
    public void updateMsgMailTemplate(MsgMailTemplate msgMailTemplate) {
        msgMailTemplateMapper.updateById(msgMailTemplate);
    }

    @Override
    public void deleteMsgMailTemplate(Long[] ids) {
        msgMailTemplateMapper.deleteBatchIds(Arrays.asList(ids));
    }

    private LambdaQueryWrapper<MsgMailTemplate> wrapperBuilder(MsgMailTemplate msgMailTemplate) {
        Map<String, Object> params = msgMailTemplate.getParams();
        LambdaQueryWrapper<MsgMailTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(msgMailTemplate.getMailType()), MsgMailTemplate::getMailType, msgMailTemplate.getMailType())
                .like(ObjectUtils.isNotEmpty(msgMailTemplate.getMailSubject()), MsgMailTemplate::getMailSubject, msgMailTemplate.getMailSubject())
                .like(ObjectUtils.isNotEmpty(msgMailTemplate.getTempName()), MsgMailTemplate::getTempName, msgMailTemplate.getTempName())
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        MsgMailTemplate::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(MsgMailTemplate::getCreateTime);
        return wrapper;
    }
}
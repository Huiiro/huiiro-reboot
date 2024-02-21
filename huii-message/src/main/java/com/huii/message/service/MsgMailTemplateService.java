package com.huii.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.message.domain.MsgMailTemplate;

import java.util.List;

/**
 * 邮件模板服务层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
public interface MsgMailTemplateService extends IService<MsgMailTemplate> {

    /**
     * 查询邮件模板分页
     *
     * @param msgMailTemplate msgMailTemplate
     * @param pageParam       pageParam
     * @return page
     */
    Page selectMsgMailTemplateList(MsgMailTemplate msgMailTemplate, PageParam pageParam);


    /**
     * 获取label
     *
     * @return list
     */
    List<Label> getLabelList();

    /**
     * 查询邮件模板
     *
     * @param id id
     * @return msgMailTemplate
     */
    MsgMailTemplate selectMsgMailTemplateById(Long id);

    /**
     * 校验添加邮件模板数据
     *
     * @param msgMailTemplate msgMailTemplate
     */
    void checkInsert(MsgMailTemplate msgMailTemplate);

    /**
     * 添加邮件模板
     *
     * @param msgMailTemplate msgMailTemplate
     */
    void insertMsgMailTemplate(MsgMailTemplate msgMailTemplate);

    /**
     * 校验修改邮件模板数据
     *
     * @param msgMailTemplate msgMailTemplate
     */
    void checkUpdate(MsgMailTemplate msgMailTemplate);

    /**
     * 修改邮件模板
     *
     * @param msgMailTemplate msgMailTemplate
     */
    void updateMsgMailTemplate(MsgMailTemplate msgMailTemplate);

    /**
     * 删除邮件模板
     *
     * @param ids ids
     */
    void deleteMsgMailTemplate(Long[] ids);

}
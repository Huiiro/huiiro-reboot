package com.huii.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.message.domain.MsgSendTemplate;

/**
 * 推送模板服务层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
public interface MsgSendTemplateService extends IService<MsgSendTemplate> {

    /**
     * 查询推送模板分页
     *
     * @param msgSendTemplate msgSendTemplate
     * @param pageParam       pageParam
     * @return page
     */
    Page selectMsgSendTemplateList(MsgSendTemplate msgSendTemplate, PageParam pageParam);

    /**
     * 查询推送模板
     *
     * @param id id
     * @return msgSendTemplate
     */
    MsgSendTemplate selectMsgSendTemplateById(Long id);

    /**
     * 执行推送任务
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void runMessageSend(MsgSendTemplate msgSendTemplate);

    /**
     * 校验添加推送模板数据
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void checkInsert(MsgSendTemplate msgSendTemplate);

    /**
     * 添加推送模板
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void insertMsgSendTemplate(MsgSendTemplate msgSendTemplate);

    /**
     * 校验修改推送模板数据
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void checkUpdate(MsgSendTemplate msgSendTemplate);

    /**
     * 修改推送模板
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void updateMsgSendTemplate(MsgSendTemplate msgSendTemplate);

    /**
     * 删除推送模板
     *
     * @param ids ids
     */
    void deleteMsgSendTemplate(Long[] ids);

}
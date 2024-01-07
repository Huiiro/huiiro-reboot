package com.huii.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.message.domain.MsgSendTemplate;

/**
 * 发送模板服务层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
public interface MsgSendTemplateService extends IService<MsgSendTemplate> {

    /**
     * 查询发送模板分页
     *
     * @param msgSendTemplate msgSendTemplate
     * @param pageParam pageParam
     * @return page
     */
    Page selectMsgSendTemplateList(MsgSendTemplate msgSendTemplate, PageParam pageParam);

    /**
     * 查询发送模板
     *
     * @param id id
     * @return msgSendTemplate
     */
    MsgSendTemplate selectMsgSendTemplateById(Long id);

    /**
     * 执行发送任务
     * @param msgSendTemplate msgSendTemplate
     */
    void runMessageSend(MsgSendTemplate msgSendTemplate);

    /**
     * 校验添加发送模板数据
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void checkInsert(MsgSendTemplate msgSendTemplate);

    /**
     * 添加发送模板
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void insertMsgSendTemplate(MsgSendTemplate msgSendTemplate);

    /**
     * 校验修改发送模板数据
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void checkUpdate(MsgSendTemplate msgSendTemplate);

    /**
     * 修改发送模板
     *
     * @param msgSendTemplate msgSendTemplate
     */
    void updateMsgSendTemplate(MsgSendTemplate msgSendTemplate);

    /**
     * 删除发送模板
     *
     * @param ids ids
     */
    void deleteMsgSendTemplate(Long[] ids);

}
package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysMessage;
import com.huii.system.domain.dto.SysMessageDto;

public interface SysMessageService {

    /**
     * 获取未读消息数量
     */
    Long getUnReadMessageCount(Long id);

    /**
     * 查询系统消息分页
     *
     * @param sysMessage sysMessage
     * @param pageParam  pageParam
     * @return page
     */
    Page selectMessageList(SysMessage sysMessage, PageParam pageParam);

    /**
     * 查询系统消息分页
     *
     * @param sysMessage sysMessage
     * @param pageParam  pageParam
     * @return page
     */
    Page selectMyMessageList(SysMessage sysMessage, PageParam pageParam);

    /**
     * 查询系统消息
     *
     * @param id id
     * @return sysMessage
     */
    SysMessage selectMessageById(Long id);

    /**
     * 校验插入数据
     *
     * @param sysMessage sysMessage
     */
    void checkInsert(SysMessageDto sysMessage);

    /**
     * 添加系统消息
     *
     * @param sysMessage sysMessageDto
     */
    void insertMessage(SysMessageDto sysMessage);

    /**
     * 添加系统消息（内部API 不对外调用）
     *
     * @param sysMessage sysMessage
     */
    void insertMessage(SysMessage sysMessage);

    /**
     * 校验修改数据
     *
     * @param sysMessage sysMessage
     */
    void checkUpdate(SysMessage sysMessage);

    /**
     * 修改系统消息
     *
     * @param sysMessage sysMessage
     */
    void updateMessage(SysMessage sysMessage);

    /**
     * 删除系统消息
     *
     * @param ids ids
     */
    void deleteMessage(Long[] ids);

}

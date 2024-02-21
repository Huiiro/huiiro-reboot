package com.huii.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.message.domain.MsgSendLog;

import java.util.List;

/**
 * 推送日志服务层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
public interface MsgSendLogService extends IService<MsgSendLog> {

    /**
     * 查询消息推送日志分页
     *
     * @param msgSendLog msgSendLog
     * @param pageParam  pageParam
     * @return page
     */
    Page selectMsgSendLogList(MsgSendLog msgSendLog, PageParam pageParam);

    /**
     * 查询消息推送日志集合
     *
     * @param msgSendLog msgSendLog
     * @return list
     */
    List<MsgSendLog> selectMsgSendLogList(MsgSendLog msgSendLog);

    /**
     * 删除消息推送日志
     *
     * @param ids ids
     */
    void deleteMsgSendLog(Long[] ids);

    /**
     * 删除全部消息推送日志
     */
    void deleteMsgSendLogAll();
}

package com.huii.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.message.domain.MsgSubscribe;

import java.util.List;

/**
 * 订阅服务层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
public interface MsgSubscribeService extends IService<MsgSubscribe> {

    Page selectMsgSubscribeList(MsgSubscribe msgSubscribe, PageParam pageParam);

    Page selectMsgSubscribeUserList(Long userId, MsgSubscribe msgSubscribe, PageParam pageParam);

    List<Label> getLabelList();

    MsgSubscribe selectMsgSubscribeById(Long id);

    void checkInsert(MsgSubscribe msgSubscribe);

    void insertMsgSubscribe(MsgSubscribe msgSubscribe);

    void checkUpdate(MsgSubscribe msgSubscribe);

    void updateMsgSubscribe(MsgSubscribe msgSubscribe);

    void deleteMsgSubscribe(Long[] ids);

    void subscribe(Long userId, Long id);

    List<Long> queryNonSubscribeUserIds(Long sid);

    List<Long> querySubscribeUserIds(Long sid);

    void subscribeUser(Long sid, Long[] userIds);

    void unsubscribeUser(Long sid, Long[] userIds);
}

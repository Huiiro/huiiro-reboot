package com.huii.message.mapper;

import com.huii.common.core.model.base.BaseMapperPlus;
import com.huii.message.domain.MsgSendLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息推送日志数据层接口
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Mapper
public interface MsgSendLogMapper extends BaseMapperPlus<MsgSendLog> {
}

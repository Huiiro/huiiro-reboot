package com.huii.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息发送类型枚举
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum MsgSendType {

    SMS("1"),
    MAIL("2");

    private final String value;
}

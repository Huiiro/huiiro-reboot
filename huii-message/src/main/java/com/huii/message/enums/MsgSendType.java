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

    SMS("1", "短信"),
    MAIL("2", "邮件");

    private final String value;

    private final String name;

    public static String getNameByValue(String value) {
        for (MsgSendType type : MsgSendType.values()) {
            if (type.getValue().equals(value)) {
                return type.getName();
            }
        }
        return null;
    }
}

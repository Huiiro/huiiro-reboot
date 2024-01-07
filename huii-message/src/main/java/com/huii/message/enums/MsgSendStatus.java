package com.huii.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum MsgSendStatus {

    TO_SEND("1"),
    SEND_SUC("2"),
    SEND_FAIL("3");

    private final String value;
}

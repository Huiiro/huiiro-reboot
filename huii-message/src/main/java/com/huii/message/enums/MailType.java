package com.huii.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum MailType {

    TEXT("1"),
    HTML("2"),
    ATTACH("3");

    private final String value;
}

package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型枚举类
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum LoginType {

    ACCOUNT(1, "账号登录"),
    EMAIL(2, "邮箱登录"),
    SMS(3, "手机登录");

    private final Integer id;
    private final String key;
}

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

    ACCOUNT(1, "account", "账号登录"),
    EMAIL(2, "email", "邮箱登录"),
    SMS(3, "sms", "手机登录"),
    GITHUB(4, "github", "Github登录"),
    GITEE(5, "gitee", "Gitee登录"),
    ;

    private final Integer id;
    private final String name;
    private final String key;

    public static LoginType getLoginType(Integer id) {
        if (id < 0) {
            return null;
        }
        for (LoginType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }
}

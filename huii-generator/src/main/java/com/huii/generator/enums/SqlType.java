package com.huii.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum SqlType {

    MYSQL("1", "mysql", true),
    POSTGRESQL("2", "postgresql", true),
    ORACLE("3", "oracle", false),
    SQLSERVER("4", "sqlserver", false);

    private final String id;
    private final String name;
    private final boolean support;

    public static boolean support(String id) {
        for (SqlType value : values()) {
            if (value.id.equals(id)) {
                return value.support;
            }
        }
        return false;
    }
}

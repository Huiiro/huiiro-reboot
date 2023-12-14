package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据权限类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum DataScopeType {

    /**
     * 全部数据权限
     */
    ALL("1"),

    /**
     * 自定数据权限
     */
    CUSTOM("2"),

    /**
     * 部门数据权限
     */
    DEPT("3"),

    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD("4"),

    /**
     * 仅本人数据权限
     */
    SELF("5");

    private final String code;

    public static DataScopeType getScope(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (DataScopeType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}

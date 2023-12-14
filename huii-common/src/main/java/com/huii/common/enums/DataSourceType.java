package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据源类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum DataSourceType {

    /**
     * MySQL
     */
    MY_SQL("MySQL"),

    /**
     * PostgreSQL
     */
    POSTGRE_SQL("PostgreSQL"),

    /**
     * Oracle
     */
    ORACLE("Oracle"),

    /**
     * SQL Server
     */
    SQL_SERVER("Microsoft SQL Server");

    private final String type;

    public static DataSourceType find(String dataSourceName) {
        if (StringUtils.isBlank(dataSourceName)) {
            return null;
        }
        for (DataSourceType type : values()) {
            if (type.getType().equals(dataSourceName)) {
                return type;
            }
        }
        return null;
    }
}

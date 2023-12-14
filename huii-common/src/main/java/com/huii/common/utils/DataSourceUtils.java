package com.huii.common.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.huii.common.enums.DataSourceType;
import com.huii.common.exception.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库类型获取工具
 *
 * @author huii
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSourceUtils {

    private static final DynamicRoutingDataSource DS = SpringUtils.getBean(DynamicRoutingDataSource.class);

    public static DataSourceType getDataBaseType() {
        DataSource dataSource = DS.determineDataSource();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            return DataSourceType.find(databaseProductName);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public static boolean isMySql() {
        return DataSourceType.MY_SQL == getDataBaseType();
    }

    public static boolean isOracle() {
        return DataSourceType.ORACLE == getDataBaseType();
    }

    public static boolean isPostgreSql() {
        return DataSourceType.POSTGRE_SQL == getDataBaseType();
    }

    public static boolean isSqlServer() {
        return DataSourceType.SQL_SERVER == getDataBaseType();
    }
}

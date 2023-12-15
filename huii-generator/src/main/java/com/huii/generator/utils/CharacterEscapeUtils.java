package com.huii.generator.utils;

import com.huii.common.utils.DataSourceUtils;

import java.util.Arrays;
import java.util.List;

public class CharacterEscapeUtils {

    /**
     * 数据库字段只提供主要类型，可根据需要自行添加
     */
    private static final Boolean enableSqlTypeCheck = false;

    public static final String PASCAL_REGEX = "^[A-Z][a-zA-Z0-9]*$";

    public static final String CAMEL_REGEX = "^[a-z][a-zA-Z0-9]*$";

    public static final String UNDER_SCORE_REGEX = "^[a-z]+(_[a-z0-9]+)*$";

    public static final String PACKAGE_NAME_REGEX = "^([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*$";

    private static final List<String> MYSQL_DATA_BASE_TYPES = Arrays.asList(
            "int", "bigint", "decimal", "double", "char",
            "varchar", "text", "longtext", "blob", "longblob",
            "date", "datetime"
    );

    private static final List<String> PGSQL_DATA_BASE_TYPES = Arrays.asList(
            "integer", "smallint", "bigint", "float4", "float8",
            "smallserial", "serial", "bigserial", "money", "varchar",
            "char", "text", "timestamp", "timestamp without time zone",
            "time with time zone", "date", "interval", "bool", "json"
    );

    private static final List<String> ORACLE_DATA_BASE_TYPES = Arrays.asList(
            "NUMBER", "INTEGER", "FLOAT", "DOUBLE PRECISION",
            "VARCHAR2", "CHAR", "CLOB", "BLOB",
            "DATE", "TIMESTAMP"
    );

    private static final List<String> SQL_SERVER_DATA_TYPES = Arrays.asList(
            "int", "bigint", "decimal", "float",
            "varchar", "char", "text", "nvarchar", "nchar", "ntext",
            "varbinary", "binary",
            "date", "datetime"
    );

    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
            "public", "private", "protected", "static", "final", "abstract",
            "synchronized", "volatile", "native", "transient", "strictfp"
    );

    private static final List<String> JAVA_TYPES = Arrays.asList(
            "Long", "Integer", "Double", "String", "BigDecimal",
            "LocalDateTime", "Date", "Boolean"
    );

    /**
     * 下划线转驼峰
     * <p>sys_user_post => sysUserPost</p>
     */
    public static String underscoreToCamel(String input) {
        if (input == null || !input.contains("_")) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNext = false;
            }
        }
        return result.toString();
    }

    /**
     * 下划线转帕斯卡
     * <p>sys_user_post => SysUserPost</p>
     */
    public static String underscoreToPascal(String input) {
        if (input == null || !input.contains("_")) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNext = false;
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线
     * <p>sysUserPost => sys_user_post</p>
     */
    public static String camelToUnderscore(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append('_').append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * 帕斯卡转下划线
     * <p>SysUserPost => sys_user_post</p>
     */
    public static String pascalToUnderscore(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * 将字符串的首字母转为大写
     * <p>abc => Abc</p>
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return str;
        }

        return Character.toUpperCase(firstChar) + str.substring(1);
    }

    /**
     * 将字符串的首字母转为小写
     * <p>ABC => aBC</p>
     */
    public static String lowercaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return str;
        }

        return Character.toLowerCase(firstChar) + str.substring(1);
    }

    /**
     * 判断首字母是否是大写
     */
    public static Boolean isFirstLetterCapitalize(String str) {
        char charAt = str.charAt(0);
        return Character.isUpperCase(charAt);
    }

    /**
     * 判断首字母是否是小写
     */
    public static Boolean isFirstLetterLowercase(String str) {
        char charAt = str.charAt(0);
        return Character.isLowerCase(charAt);
    }

    /**
     * 验证帕斯卡命名
     */
    public static boolean isPascalCase(String name) {
        return name.matches(PASCAL_REGEX);
    }

    /**
     * 验证驼峰命名
     */
    public static boolean isCamelCase(String name) {
        return name.matches(CAMEL_REGEX);
    }

    /**
     * 验证下划线命名
     */
    public static boolean isUnderscoreCase(String name) {
        return name.matches(UNDER_SCORE_REGEX);
    }

    /**
     * 验证是否符合包名规范 com.example.name
     */
    public static boolean isValidPackageName(String name) {
        return name.matches(PACKAGE_NAME_REGEX);
    }

    /**
     * 验证JAVA属性是否正确
     */
    public static boolean isJavaProperty(String keyword) {
        return JAVA_KEYWORDS.contains(keyword);
    }

    /**
     * 验证JAVA类型是否正确
     */
    public static boolean isJavaType(String keyword) {
        return JAVA_TYPES.contains(keyword);
    }

    /**
     * 验证数据库字段类型是否正确
     */
    public static boolean isDataBaseType(String keyword) {
        if (!enableSqlTypeCheck) {
            return true;
        }
        if (DataSourceUtils.isMySql()) {
            return MYSQL_DATA_BASE_TYPES.contains(keyword);
        } else if (DataSourceUtils.isPostgreSql()) {
            return PGSQL_DATA_BASE_TYPES.contains(keyword);
        } else if (DataSourceUtils.isOracle()) {
            return ORACLE_DATA_BASE_TYPES.contains(keyword);
        } else if (DataSourceUtils.isSqlServer()) {
            return SQL_SERVER_DATA_TYPES.contains(keyword);
        } else {
            return false;
        }
    }

}

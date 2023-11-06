package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举类
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum OpType {

    OTHER(1, "other", "其它"),
    QUERY(2, "query", "查询"),
    GRANT(3, "grant", "授权"),
    INSERT(4, "insert", "新增"),
    UPDATE(5, "update", "修改"),
    DELETE(6, "delete", "删除"),
    IMPORT(7, "import", "导入"),
    EXPORT(8, "export", "导出"),
    GENERATE(9, "generate", "生成"),
    CLEAN(10, "clean", "清空");

    private final Integer id;
    private final String name;
    private final String key;
}

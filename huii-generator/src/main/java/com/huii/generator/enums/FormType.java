package com.huii.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表单类型枚举
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum FormType {

    INPUT("input"),
    TEXTAREA("textarea"),
    RICHTEXT("richtext"),
    SELECT("select"),
    CHECKBOX("checkbox"),
    DATETIME("datetime");
    //TODO

    private final String type;
}

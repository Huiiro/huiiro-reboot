package com.huii.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormType {

    INPUT("input"),
    TEXTAREA("textarea"),
    SELECT("select"),
    CHECKBOX("checkbox"),
    DATETIME("datetime");
    //TODO

    private final String type;
}

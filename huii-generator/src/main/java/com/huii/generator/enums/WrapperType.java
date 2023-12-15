package com.huii.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WrapperType {

    EQ("eq"),
    LIKE("like"),
    NE("ne");
    //TODO

    private final String type;
}

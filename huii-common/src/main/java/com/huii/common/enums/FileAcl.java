package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileAcl {

    PUBLIC("pub"),

    PRIVATE("pri");

    private final String acl;
}

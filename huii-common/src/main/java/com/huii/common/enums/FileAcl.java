package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件权限
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum FileAcl {

    PUBLIC("pub"),

    PRIVATE("pri");

    private final String acl;
}

package com.huii.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadType {

    LOCAL("local"),
    OSS("oss");

    private final String name;
}

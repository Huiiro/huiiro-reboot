package com.huii.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件上传类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum UploadType {

    LOCAL("local"),
    OSS("oss");

    private final String name;
}

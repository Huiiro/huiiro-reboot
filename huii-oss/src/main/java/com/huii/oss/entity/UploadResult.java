package com.huii.oss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 上传返回体
 *
 * @author huii
 */
@Data
@Builder
@AllArgsConstructor
public class UploadResult {

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件原名
     */
    private String fileOriginName;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件MD5
     */
    private String md5;

}

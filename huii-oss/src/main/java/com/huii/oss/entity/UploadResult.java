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
}

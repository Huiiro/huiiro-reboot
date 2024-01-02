package com.huii.oss.core.service;

import com.amazonaws.services.s3.model.S3Object;

/**
 * oss文件存储服务
 *
 * @author huii
 */
public interface OssService extends FileService {

    /**
     * 获取文件s3信息
     *
     * @param fileName 文件名
     * @return s3Object
     */
    S3Object getObject(String fileName);
}

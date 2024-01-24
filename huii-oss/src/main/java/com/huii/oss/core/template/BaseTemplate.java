package com.huii.oss.core.template;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.huii.oss.entity.UploadResult;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.List;

/**
 * 文件操作基本模板
 *
 * @author huii
 */
public interface BaseTemplate extends InitializingBean {

    @Override
    default void afterPropertiesSet() {
    }

    /**
     * 创建 bucket
     *
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 删除 bucket
     *
     * @param bucketName bucket名称
     */
    void removeBucket(String bucketName);

    /**
     * 获取全部 bucket
     *
     * @return Bucket 列表
     */
    List<Bucket> getAllBuckets();

    /**
     * 根据 bucket 获取 bucket 详情
     *
     * @param bucketName bucket名称
     * @return Bucket ? null
     */
    Bucket getBucket(String bucketName);

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     */
    UploadResult putObject(String bucketName, String objectName, InputStream stream, String contextType);

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     */
    UploadResult putObject(String bucketName, String objectName, InputStream stream);

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    S3Object getObject(String bucketName, String objectName);

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    InputStream getStream(String bucketName, String objectName);

    /**
     * 获取文件直链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return directUrl
     */
    String getDirectUrl(String bucketName, String objectName);

    /**
     * 获取文件外联
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <= 7 d
     * @return preSignedUrl
     */
    String getPreSignedUrl(String bucketName, String objectName, Integer expires);

    /**
     * 删除文件
     *
     * @param bucketName bucketName
     * @param objectName objectName
     */
    void deleteObject(String bucketName, String objectName);

    /**
     * 校验文件是否存在
     *
     * @param bucketName bucketName
     * @param fileName   fileName
     * @return b
     */
    boolean exists(String bucketName, String fileName);
}

package com.huii.oss.core.service;

import com.huii.oss.entity.UploadResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 本地文件存储服务
 *
 * @author huii
 */
public interface LocalService extends FileService {

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param module        文件所属模块
     * @return result
     */
    UploadResult uploadFile(MultipartFile multipartFile, String module);

    /**
     * 获取文件流
     *
     * @param fileName 文件名
     * @param module   文件所属模块
     * @return stream
     */
    InputStream getFileStream(String fileName, String module);

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response 响应体
     * @param module   文件所属模块
     */
    void downloadFile(String fileName, HttpServletResponse response, String module);

    /**
     * 获取文件直链
     *
     * @param fileName fileName
     * @param module   文件所属模块
     * @return directUrl
     */
    String getDirectUrl(String fileName, String module);
}

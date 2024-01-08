package com.huii.oss.core.service;

import com.huii.oss.entity.UploadResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件服务
 *
 * @author huii
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file file
     * @return result
     */
    UploadResult uploadFile(MultipartFile file);

    /**
     * 获取文件流
     *
     * @param fileName 文件名
     * @return stream
     */
    InputStream getFileStream(String fileName);

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response 响应体
     */
    void downloadFile(String fileName, HttpServletResponse response);

    /**
     * 获取文件直链
     *
     * @param fileName fileName
     * @return directUrl
     */
    String getDirectUrl(String fileName);

    /**
     * 获取文件外链
     *
     * @param fileName 文件名
     * @return preSignedUrl
     */
    String getPreSignedUrl(String fileName);

    /**
     * 通过url进行删除
     *
     * @param url url
     */
    void deleteByUrl(String url);

    /**
     * 通过文件名批量删除
     *
     * @param names nameIds
     */
    void deleteBatch(List<String> names);
}

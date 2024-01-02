package com.huii.oss.core.service.impl;

import com.huii.oss.config.properties.LocalProperties;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.exception.OssException;
import com.huii.oss.utils.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class LocalServiceImpl implements LocalService {

    private static final String DEFAULT_MODULE = "default";
    private final LocalProperties properties;

    @Override
    public UploadResult uploadFile(MultipartFile file) {
        return uploadFile(file, DEFAULT_MODULE);
    }

    @Override
    public UploadResult uploadFile(MultipartFile multipartFile, String module) {
        String fileName = FileUtils.initRandomFileName() +
                FileUtils.getFileSuffixWithDot(multipartFile.getOriginalFilename());
        //本地保存路径，如：D:/huii/file/avatar/fa98ft91g981.jpg
        String savePath = properties.getBasePath() + "/" + module + "/" + fileName;
        //网页访问路径，如：https:ss.huii147.xyz/avatar/fa98ft91g981.jpg
        String fileUrl = properties.getEndPoint() + "/" + module + "/" + fileName;
        File file = new File(savePath);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            throw new OssException("文件读取异常");
        }
        return new UploadResult(fileUrl, multipartFile.getOriginalFilename());
    }

    @Override
    public InputStream getFileStream(String fileName) {
        return getFileStream(fileName, DEFAULT_MODULE);
    }

    @Override
    public InputStream getFileStream(String fileName, String module) {
        String path = properties.getBasePath() + "/" + module + "/" + fileName;
        try {
            return Files.newInputStream(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response) {
        downloadFile(fileName, response, DEFAULT_MODULE);
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response, String module) {
        FileUtils.downloadFile(fileName, response, getFileStream(fileName, module));
    }

    @Override
    public String getDirectUrl(String fileName) {
        return getDirectUrl(fileName, DEFAULT_MODULE);
    }

    @Override
    public String getDirectUrl(String fileName, String module) {
        return properties.getEndPoint() + "/" + module + "/" + fileName;
    }

    @Override
    public String getPreSignedUrl(String fileName) {
        return null;
    }

}

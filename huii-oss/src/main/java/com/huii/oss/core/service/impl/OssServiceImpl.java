package com.huii.oss.core.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.oss.config.properties.OssProperties;
import com.huii.oss.core.service.OssService;
import com.huii.oss.core.template.OssTemplate;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.utils.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final OssTemplate ossTemplate;
    private final OssProperties ossProperties;

    @Override
    public UploadResult uploadFile(MultipartFile file) {
        try {
            return ossTemplate.putObject(ossProperties.getBucketName(), file.getOriginalFilename(),
                    file.getInputStream(), file.getContentType());
        } catch (IOException e) {
            ResType resType = ResType.SYS_FILE_UPLOAD_FAIL;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public InputStream getFileStream(String fileName) {
        if (ossTemplate.exists(ossProperties.getBucketName(), fileName)) {
            return ossTemplate.getStream(ossProperties.getBucketName(), fileName);
        }
        ResType resType = ResType.SYS_FILE_NOT_EXIST;
        throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response) {
        FileUtils.downloadFile(fileName, response, getFileStream(fileName));
    }

    @Override
    public S3Object getObject(String fileName) {
        if (ossTemplate.exists(ossProperties.getBucketName(), fileName)) {
            return ossTemplate.getObject(ossProperties.getBucketName(), fileName);
        }
        ResType resType = ResType.SYS_FILE_NOT_EXIST;
        throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
    }

    @Override
    public String getDirectUrl(String fileName) {
        try {
            return ossTemplate.getDirectUrl(ossProperties.getBucketName(), fileName);
        } catch (Exception e) {
            ResType resType = ResType.SYS_FILE_NOT_EXIST;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public String getPreSignedUrl(String fileName) {
        try {
            return ossTemplate.getPreSignedUrl(ossProperties.getBucketName(), fileName, 7);
        } catch (Exception e) {
            ResType resType = ResType.SYS_FILE_NOT_EXIST;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    @Override
    public void deleteByUrl(String url) {
        //http://192.168.24.101:9001/local/202401090047e95113eb2b2a.jpg
        //TODO
    }

    @Override
    public void deleteBatch(List<String> names) {
        try {
            for (String name : names) {
                String substring = name.substring(name.indexOf('/', name.indexOf("//") + 2) + 1);
                String[] parts = substring.split("/");
                if (parts.length >= 2) {
                    ossTemplate.deleteObject(parts[parts.length - 2], parts[parts.length - 1]);
                }
            }
        } catch (Exception e) {
            ResType resType = ResType.SYS_FILE_DELETE_FAIL;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }
}

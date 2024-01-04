package com.huii.oss.core.service.impl;

import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.oss.config.properties.LocalProperties;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.utils.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            String fileName = FileUtils.initRandomFileName() +
                    FileUtils.getFileSuffixWithDot(multipartFile.getOriginalFilename());
            //本地保存路径，如：D:/huii/file/avatar/fa98ft91g981.jpg
            String savePath = properties.getBasePath() + "/" + module + "/" + fileName;
            //网页访问路径，如：https:api.huii147.xyz/oss/local?module=default&fileName=fa98ft91g981.jpg
            String fileUrl = properties.getEndPoint() + "?module=" + module + "&fileName=" + fileName;
            //检查文件目录是否存在
            FileUtils.createDirectory(savePath);
            File file = new File(savePath);
            //计算md5
            String md5 = DigestUtils.md5Hex(multipartFile.getInputStream());
            multipartFile.transferTo(file);
            return new UploadResult(fileUrl, fileName, multipartFile.getOriginalFilename(),
                    FileUtils.formatFileSize(multipartFile.getSize()), md5);
        } catch (Exception e) {
            ResType resType = ResType.SYS_FILE_UPLOAD_FAIL;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
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
            ResType resType = ResType.SYS_FILE_NOT_EXIST;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
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

    @Override
    public void deleteBatch(List<String> names) {
        String basePath = properties.getBasePath();
        try {
            for (String name : names) {
                String query = new URL(name).getQuery();
                Map<String, String> params = getQueryParams(query);
                String filePath = basePath + "/" + params.get("module") + "/" + params.get("fileName");
                Path path = Paths.get(filePath);
                Files.delete(path);
            }
        } catch (IOException e) {
            ResType resType = ResType.SYS_FILE_DELETE_FAIL;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    private static Map<String, String> getQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }
}

package com.huii.oss.core.controller;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.base.BaseController;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.exception.OssException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;


@RestController
@RequestMapping("/oss/local")
@RequiredArgsConstructor
public class LocalFileController extends BaseController {

    private final LocalService localService;

    @Anonymous
    @RequestMapping("")
    @SneakyThrows
    public void loadFile(@RequestParam String module, @RequestParam String fileName,
                         @RequestParam(required = false) Boolean download,
                         HttpServletResponse response) {
        InputStream fileStream = localService.getFileStream(fileName, module);

        if (ObjectUtils.isNotEmpty(download) && download) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else {
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        }
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            fileStream.close();
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new OssException("文件读取失败");
        }
    }
}

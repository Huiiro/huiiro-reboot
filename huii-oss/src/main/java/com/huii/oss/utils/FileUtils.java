package com.huii.oss.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author huii
 */
public class FileUtils {

    private static final String[] SUFFIXES = {
            "pdf", "txt", "htm", "html",
            "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "rar", "zip", "gz", "7z", "tar",
            "gif", "jpg", "jpeg", "png", "svg", "ai",
            "avi", "mp4", "mkv", "mp3", "WMV"
    };

    /**
     * 校验文件是否为空
     *
     * @param multipartFile multipartFile
     * @return b
     */
    public static boolean checkFileEmpty(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty() &&
                multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().isEmpty();
    }

    /**
     * 获取文件后缀名 该方法会返回带dot的文件名 如.jpg
     *
     * @param originalFilename fileName
     * @return fileSuffix
     */
    public static String getFileSuffixWithDot(String originalFilename) {
        return StringUtils.substring(originalFilename, originalFilename.lastIndexOf("."), originalFilename.length());
    }

    /**
     * 获取文件后缀名 该方法会返回不带dot的文件名 如jpg
     *
     * @param originalFilename fileName
     * @return fileSuffix
     */
    public static String getFileSuffix(String originalFilename) {
        return StringUtils.substringAfterLast(originalFilename, ".");
    }


    /**
     * 校验文件后缀名是否符合要求
     *
     * @param originalFilename fileName
     * @return b
     */
    public static boolean checkFileSuffix(String originalFilename) {
        return checkFileSuffix(originalFilename, Arrays.asList(SUFFIXES));
    }

    /**
     * 校验文件后缀名是否符合自定义要求
     *
     * @param originalFilename fileName
     * @param suffixes         自定义后缀名集合
     * @return b
     */
    public static boolean checkFileSuffix(String originalFilename, List<String> suffixes) {
        String suffix = getFileSuffix(originalFilename);
        boolean suffixFlag = false;
        for (String s : suffixes) {
            if (s.equalsIgnoreCase(suffix)) {
                suffixFlag = true;
                break;
            }
        }
        return suffixFlag;
    }

    /**
     * 美化文件大小格式，保留两位结果
     *
     * @param length file.size()
     * @return formatted str
     */
    public static String formatFileSize(double length) {
        if (length < 1024) {
            return length + "B";
        } else {
            length = length / 1024.0;
        }
        if (length < 1024) {
            return Math.round(length * 100) / 100.0 + "KB";
        } else {
            length = length / 1024.0;
        }
        if (length < 1024) {
            return Math.round(length * 100) / 100.0 + "MB";
        } else {
            return Math.round(length / 1024 * 100) / 100.0 + "GB";
        }
    }

    /**
     * 将 MultipartFile转为 File
     *
     * @param file MultipartFile
     * @return File
     * @throws Exception e
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            OutputStream os = Files.newOutputStream(toFile.toPath());
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        }
        return toFile;
    }

    public static String initRandomFileName() {
        StringBuilder builder = new StringBuilder();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String uuid = UUID.randomUUID().toString().substring(0, 12);
        return builder.append(time).append(uuid).toString();
    }

    public static void downloadFile(String fileName, HttpServletResponse response, InputStream is) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) > 0) {
                os.write(b, 0, len);
            }
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败");
        }
    }

    /**
     * 创建文件目录
     *
     * @param directoryPath dirPath
     */
    public static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("目录创建失败");
            }
        }
    }
}

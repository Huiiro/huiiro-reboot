package com.huii.oss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置--本地
 * local
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "local")
public class LocalProperties {

    /**
     * 本地文件保存根路径 目录最后不带'/'号
     * <p>windows: D:/huii/file</p>
     * <p>linux: /home/huii/file</p>
     */
    private String basePath;

    /**
     * 文件访问路径 目录最后不带'/'号
     * <p>https:www.huii147.xyz/res</p>
     */
    private String endPoint;
}

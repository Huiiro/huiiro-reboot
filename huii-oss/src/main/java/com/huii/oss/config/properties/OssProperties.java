package com.huii.oss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    private String endpoint;

    private String domain;

    private String bucketName;

    private String region;

    private String accessKey;

    private String secretKey;

    private Integer maxConnections = 100;

    /**
     * 0--private
     * 1--public
     * 2--custom
     */
    private String accessPolicy = "0";

    private Boolean pathStyleAccess = true;

    private Boolean https = true;

}

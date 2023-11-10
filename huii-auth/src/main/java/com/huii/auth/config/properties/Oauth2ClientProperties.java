package com.huii.auth.config.properties;


import com.huii.auth.core.entity.oauth2.Oauth2Client;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录参数配置
 * config.login
 *
 * @author huii
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2-key")
public class Oauth2ClientProperties {

    private List<Oauth2Client> clients;
}

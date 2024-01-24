package com.huii.auth.core.entity.oauth2;

import lombok.Data;

import java.util.List;

/**
 * oauth2认证信息
 *
 * @author huii
 */
@Data
public class Oauth2Client {

    private String provider;

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String authorizationUri;

    private String tokenUri;

    private String userInfoUri;

    private List<String> scope;

    private String responseType;

    private String grantType;
}

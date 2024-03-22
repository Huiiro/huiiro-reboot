package com.huii.auth.strategy;

import com.huii.auth.core.entity.oauth2.Oauth2Client;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;

/**
 * oauth2登录实现接口
 *
 * @author huii
 */
public interface Oauth2Login extends InitializingBean {

    /**
     * 获取access_token
     * 需要client_id client_secret redirect_uri grant_type params
     * 请求tokenUri获取access_token
     * 可参照文档自行实现refresh_token接口
     *
     * @param oauth2Client oauth2Client
     * @param request      request
     * @return access_token
     */
    String getAccessToken(Oauth2Client oauth2Client, HttpServletRequest request, String... params);

    /**
     * 获取用户信息
     * 需要access_token
     * 请求userInfoUri获取userInfo
     * 返回Oauth2User 可自行实现拓展
     *
     * @param oauth2Client oauth2Client
     * @param accessToken  accessToken
     * @return Oauth2User
     */
    Oauth2User getOauthUser(Oauth2Client oauth2Client, String accessToken);
}

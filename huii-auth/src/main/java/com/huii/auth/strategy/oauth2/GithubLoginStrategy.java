package com.huii.auth.strategy.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huii.auth.core.entity.oauth2.GithubUser;
import com.huii.auth.core.entity.oauth2.Oauth2Client;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.factory.Oauth2StrategyFactory;
import com.huii.auth.strategy.AbstractOauth2LoginStrategy;
import com.huii.common.exception.BasicAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * github登录策略
 *
 * @author huii
 */
@Component
public class GithubLoginStrategy extends AbstractOauth2LoginStrategy {

    @Override
    public String getAccessToken(Oauth2Client oauth2Client, HttpServletRequest request, String... params) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("code", (String) request.getAttribute("code"));
        queryParams.put("state", (String) request.getAttribute("state"));
        queryParams.put("client_id", oauth2Client.getClientId());
        queryParams.put("client_secret", oauth2Client.getClientSecret());
        queryParams.put("redirect_uri", oauth2Client.getRedirectUri());
        queryParams.put("grant_type", oauth2Client.getGrantType());
        ResponseEntity<String> response = sendPostRequest(oauth2Client.getTokenUri(), queryParams, null);
        try {
            ObjectNode jsonObject = objectMapper.createObjectNode();
            String[] keyValuePairs = Objects.requireNonNull(response.getBody()).split("&");
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    jsonObject.put(key, value);
                }
            }
            return jsonObject.get("access_token").asText();
        } catch (Exception e) {
            throw new BasicAuthenticationException("获取用户信息失败");
        }
    }

    @Override
    public Oauth2User getOauthUser(Oauth2Client oauth2Client, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        ResponseEntity<String> response = sendHttpGetRequest(oauth2Client.getUserInfoUri(), headers);
        try {
            GithubUser oauth2User = objectMapper.readValue(response.getBody(), GithubUser.class);
            oauth2User.setType("github");
            oauth2User.setName(oauth2User.getLogin());
            oauth2User.setAvatar(oauth2User.getAvatar_url());
            return oauth2User;
        } catch (JsonProcessingException e) {
            throw new BasicAuthenticationException("获取用户信息失败");
        }
    }

    @Override
    public void afterPropertiesSet() {
        Oauth2StrategyFactory.register("github", this);
    }
}

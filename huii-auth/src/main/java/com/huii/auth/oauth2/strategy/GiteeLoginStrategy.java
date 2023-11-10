package com.huii.auth.oauth2.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.huii.auth.core.entity.oauth2.GiteeUser;
import com.huii.auth.core.entity.oauth2.Oauth2Client;
import com.huii.auth.core.entity.oauth2.Oauth2User;
import com.huii.auth.factory.Oauth2StrategyFactory;
import com.huii.auth.oauth2.Oauth2LoginStrategy;
import com.huii.common.exception.BasicAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GiteeLoginStrategy extends Oauth2LoginStrategy {
    @Override
    public String getAccessToken(Oauth2Client oauth2Client, HttpServletRequest request, String... params) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("code", (String) request.getAttribute("code"));
        queryParams.put("client_id", oauth2Client.getClientId());
        queryParams.put("client_secret", oauth2Client.getClientSecret());
        queryParams.put("redirect_uri", oauth2Client.getRedirectUri());
        queryParams.put("grant_type", oauth2Client.getGrantType());
        ResponseEntity<String> response = sendPostRequest(oauth2Client.getTokenUri(), queryParams, null);
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new BasicAuthenticationException("获取信息失败");
        }
    }

    @Override
    public Oauth2User getOauthUser(Oauth2Client oauth2Client, String accessToken) {
        ResponseEntity<String> response = sendHttpGetRequest(oauth2Client.getUserInfoUri() + "?access_token=" + accessToken);
        try {
            GiteeUser oauth2User = objectMapper.readValue(response.getBody(), GiteeUser.class);
            oauth2User.setType("gitee");
            oauth2User.setAvatar(oauth2User.getAvatar_url());
            return oauth2User;
        } catch (JsonProcessingException e) {
            throw new BasicAuthenticationException("获取用户信息失败");
        }
    }

    @Override
    public void afterPropertiesSet() {
        Oauth2StrategyFactory.register("gitee",this);
    }
}

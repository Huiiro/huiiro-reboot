package com.huii.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huii.common.exception.BasicAuthenticationException;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * oauth2登录策略
 *
 * @author huii
 */
public abstract class Oauth2LoginStrategy implements Oauth2Login {

    private final RestTemplate restTemplate;
    public final ObjectMapper objectMapper;

    public Oauth2LoginStrategy() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * get request  with header params
     *
     * @param url     url
     * @param headers HttpHeaders
     * @return ResponseEntity
     */
    protected ResponseEntity<String> sendHttpGetRequest(String url, HttpHeaders headers) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            if (Objects.equals(e.getStatusCode().value(), 401)) {
                throw new BasicAuthenticationException("认证失败，即将返回");
            } else {
                throw new BasicAuthenticationException("遇到了些问题，请稍后尝试");
            }
        }
    }

    /**
     * get request
     *
     * @param url 'full url' or 'with no param url'
     * @return ResponseEntity
     */
    protected ResponseEntity<String> sendHttpGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        return sendHttpGetRequest(url, headers);
    }

    /**
     * get request
     *
     * @param baseUrl baseUrl
     * @param params  request params
     * @return ResponseEntity
     */
    protected ResponseEntity<String> sendHttpGetRequest(String baseUrl, Map<String, String> params) {
        return sendHttpGetRequest(urlBuilder(baseUrl, params));
    }

    /**
     * post request
     *
     * @param url    full url
     * @param params data params
     * @return ResponseEntity
     */
    protected ResponseEntity<String> sendPostRequest(String url, MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            if (Objects.equals(e.getStatusCode().value(), 401)) {
                throw new BasicAuthenticationException("认证失败，即将返回");
            } else {
                throw new BasicAuthenticationException("遇到了些问题，请稍后尝试");
            }
        }
    }

    /**
     * post request
     *
     * @param url       url
     * @param urlParams url Params
     * @param params    data params
     * @return ResponseEntity
     */
    protected ResponseEntity<String> sendPostRequest(String url, Map<String, String> urlParams, MultiValueMap<String, String> params) {
        String finalUrl = urlBuilder(url, urlParams);
        return sendPostRequest(finalUrl, params);
    }

    private String urlBuilder(String baseUrl, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }
}

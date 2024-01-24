package com.huii.auth.core.entity.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * oauth2登录用户
 *
 * @author huii
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Oauth2User {

    @JsonIgnore
    private String type;

    private Long id;

    private String name;

    private String avatar;
}

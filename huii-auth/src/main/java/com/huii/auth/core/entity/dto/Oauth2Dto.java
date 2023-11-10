package com.huii.auth.core.entity.dto;

import com.huii.auth.core.entity.LoginEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oauth2登录对象
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Oauth2Dto extends LoginEntity {

    private String code;

    private String type;

    private String state;

    private String scope;
}

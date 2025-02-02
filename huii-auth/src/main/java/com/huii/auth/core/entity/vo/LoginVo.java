package com.huii.auth.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录成功返回对象
 *
 * @author huii
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginVo {

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * refreshToken
     */
    private String refreshToken;

    /**
     * loginType
     */
    private String loginType;

    /**
     * needBind
     */
    private Boolean needBind;

    /**
     * 用户信息
     * userId
     * username
     * userAvatar
     */
    private Map<String, Object> userInfo = new HashMap<>(3);

    /**
     * 用户权限
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * 用户角色
     */
    private List<String> roles = new ArrayList<>();

}

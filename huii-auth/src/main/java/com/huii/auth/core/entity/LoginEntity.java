package com.huii.auth.core.entity;

import com.huii.common.enums.LoginType;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录实体
 *
 * @author huii
 */
@Data
public class LoginEntity implements Serializable {

    /**
     * 登录类型
     */
    private LoginType loginType;

    /**
     * 用户身份标识
     */
    private String userIdentify;

    /**
     * 用户设备标识
     */
    private String userDevice;

    /**
     * 记住我
     */
    private Boolean rememberMe;

    /**
     * 验证码uuid
     */
    private String uuid;

    /**
     * 验证码值
     */
    private String code;
}

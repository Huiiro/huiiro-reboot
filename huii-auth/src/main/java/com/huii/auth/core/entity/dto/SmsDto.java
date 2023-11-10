package com.huii.auth.core.entity.dto;

import com.huii.auth.core.entity.LoginEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 手机登录对象
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsDto extends LoginEntity {

    /**
     * 手机
     */
    @NotBlank(message = "{user.phone.not.blank}")
    private String sms;

    /**
     * 验证码
     */
    @NotBlank(message = "{user.login.captcha.not.blank}")
    private String code;
}
package com.huii.auth.core.entity.dto;

import com.huii.auth.core.entity.LoginEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱登录对象
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailDto extends LoginEntity {

    /**
     * 邮箱
     */
    @NotBlank(message = "{user.email.not.blank}")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "{user.login.captcha.not.blank}")
    private String code;
}
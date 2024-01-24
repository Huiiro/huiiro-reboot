package com.huii.auth.core.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 忘记密码dto
 *
 * @author huii
 */
@Data
public class ForgetPwdEntity implements Serializable {

    @NotBlank(message = "用户名不为空")
    private String identify;

    @NotBlank(message = "验证码不为空")
    private String code;

    /**
     * enum in phone and email
     */
    private String type;

    @NotBlank(message = "密码不为空")
    private String pwd;
}

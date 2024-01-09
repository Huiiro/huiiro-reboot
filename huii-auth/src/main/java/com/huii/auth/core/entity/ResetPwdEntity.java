package com.huii.auth.core.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResetPwdEntity implements Serializable {

    @NotBlank(message = "旧密码不为空")
    private String oldPwd;

    @NotBlank(message = "新密码不为空")
    private String pwd;
}

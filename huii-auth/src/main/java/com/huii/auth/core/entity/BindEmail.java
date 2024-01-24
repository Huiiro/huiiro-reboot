package com.huii.auth.core.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 绑定邮箱dto
 *
 * @author huii
 */
@Data
public class BindEmail implements Serializable {

    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;

    private String code;

    private String type;
}

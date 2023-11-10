package com.huii.auth.core.entity.dto;

import com.huii.auth.core.entity.LoginEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;


/**
 * 用户登录对象
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountDto extends LoginEntity {

    /**
     * 用户名
     */
    @NotBlank(message = "{user.username.not.blank}")
    @Length(min = 2, max = 20, message = "{user.username.length.valid}")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = 2, max = 20, message = "{user.password.length.valid}")
    private String password;
}

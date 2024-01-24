package com.huii.auth.core.entity.token;

import com.huii.auth.core.entity.TokenEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 邮箱登录认证token
 *
 * @author huii
 */
public class EmailToken extends TokenEntity {
    public EmailToken(String email, String code) {
        super(email, code);
    }

    public EmailToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

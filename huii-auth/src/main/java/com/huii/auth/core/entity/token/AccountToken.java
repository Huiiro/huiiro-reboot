package com.huii.auth.core.entity.token;

import com.huii.auth.core.entity.TokenEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccountToken extends TokenEntity {
    public AccountToken(String username, String password) {
        super(username, password);
    }

    public AccountToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

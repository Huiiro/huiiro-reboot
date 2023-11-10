package com.huii.auth.core.entity.token;

import com.huii.auth.core.entity.TokenEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class Oauth2Token extends TokenEntity {

    private String code;
    private String state;
    private String scope;
    private String type;

    public Oauth2Token(Object principal, Object credentials, String code, String state, String scope, String type) {
        super(principal, credentials);
        this.code = code;
        this.state = state;
        this.scope = scope;
        this.type = type;
    }

    public Oauth2Token(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

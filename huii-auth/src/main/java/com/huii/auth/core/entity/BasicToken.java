package com.huii.auth.core.entity;

import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
public class BasicToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    public BasicToken(String principal, String credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
    }

    public BasicToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}

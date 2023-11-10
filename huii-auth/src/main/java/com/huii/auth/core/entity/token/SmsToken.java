package com.huii.auth.core.entity.token;

import com.huii.auth.core.entity.TokenEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SmsToken extends TokenEntity {
    public SmsToken(String sms, String code) {
        super(sms, code);
    }

    public SmsToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
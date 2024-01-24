package com.huii.auth.core.entity.token;

import com.huii.auth.core.entity.TokenEntity;
import com.huii.common.enums.LoginType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * oauth2登录认证token
 *
 * @author huii
 */
@Getter
public class Oauth2Token extends TokenEntity {

    private String code;
    private String state;
    private String scope;
    private String type;
    private LoginType loginType;
    //是否已登录并绑定该账号
    private String hasLoginAndDoBind;
    //需要绑定的账号
    private Long bindId;

    public Oauth2Token(Object principal, Object credentials, String code, String state, String scope, String type,
                       LoginType loginType, String hasLoginAndDoBind, Long bindId) {
        super(principal, credentials);
        this.code = code;
        this.state = state;
        this.scope = scope;
        this.type = type;
        this.loginType = loginType;
        this.hasLoginAndDoBind = hasLoginAndDoBind;
        this.bindId = bindId;
    }

    public Oauth2Token(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

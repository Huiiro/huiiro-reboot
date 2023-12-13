package com.huii.common.core.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.enums.LoginType;
import com.huii.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private SysUser user;
    private String principle;
    private LoginType loginType;
    private UserType userType;
    private Set<String> stringAuthorities;
    private String bindStatus = SystemConstants.STATUS_1;
    private String refreshToken;
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(SysUser user, Set<String> authorities) {
        this.user = user;
        this.stringAuthorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities = stringAuthorities.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isEnabled() {
        return user.getUserStatus().equals(SystemConstants.STATUS_1);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

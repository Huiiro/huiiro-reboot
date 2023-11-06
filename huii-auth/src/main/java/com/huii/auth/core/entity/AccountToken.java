package com.huii.auth.core.entity;

public class AccountToken extends BasicToken{
    public AccountToken(String username, String password) {
        super(username, password);
    }
}

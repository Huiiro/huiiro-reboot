package com.huii.auth.core.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterEntity implements Serializable {

    private String username;

    private String password;
}

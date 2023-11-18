package com.huii.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyPair {

    private String publicKeyPem;

    private String privateKeyPem;

    private String publicKey;

    private String privateKey;

    private byte[] publicKeyByte;

    private byte[] privateKeyByte;
}

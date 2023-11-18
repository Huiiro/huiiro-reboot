package com.huii.auth.service.impl;

import com.huii.auth.factory.SecurityKeyPairFactory;
import com.huii.auth.service.LoginSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginSecurityServiceImpl implements LoginSecurityService {

    @Override
    public String getPublicKeyPem() {
        return SecurityKeyPairFactory.getPublicKeyPem();
    }

    @Override
    public String encrypt(String rawStr) {
        return SecurityKeyPairFactory.encryptRSA(rawStr);
    }

    @Override
    public String decrypt(String encryptStr) {
        return SecurityKeyPairFactory.decryptRSA(encryptStr);
    }
}

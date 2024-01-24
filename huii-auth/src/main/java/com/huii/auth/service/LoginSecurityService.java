package com.huii.auth.service;

/**
 * 登录安全服务
 *
 * @author huii
 */
public interface LoginSecurityService {

    /**
     * 获取公钥
     *
     * @return public key
     */
    String getPublicKeyPem();

    /**
     * 解密字段
     *
     * @param rawStr rawStr
     * @return encryptStr
     */
    String encrypt(String rawStr);

    /**
     * 解密字段
     *
     * @param encryptStr encryptStr
     * @return decryptStr
     */
    String decrypt(String encryptStr);

    /**
     * 刷新accessToken
     *
     * @param refresh refresh
     * @return new token
     */
    String createNewAccessToken(String refresh);

}

package com.huii.auth.core.service.impl;

import com.huii.auth.core.service.LoginSecurityService;
import com.huii.auth.factory.SecurityKeyPairFactory;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录安全服务实现
 *
 * @author huii
 */
@Service
@RequiredArgsConstructor
public class LoginSecurityServiceImpl implements LoginSecurityService {

    private final JwtUtils jwtUtils;
    private final RedisTemplateUtils redisTemplateUtils;

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

    @Override
    public String createNewAccessToken(String refresh) {
        Long id = jwtUtils.getId(refresh);
        if (id == null || id <= -1L) {
            throw new ServiceException(MessageUtils.message(ResType.STATUS_TOKEN_EXPIRED.getI18n()));
        }
        String token = jwtUtils.createAccessToken(id);
        redisTemplateUtils.setCacheObject(CacheConstants.TOKEN + id, token, jwtUtils.getAccess(), TimeUnit.HOURS);
        return token;
    }
}

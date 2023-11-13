package com.huii.auth.service.impl;

import com.huii.async.AsyncManager;
import com.huii.async.factory.AsyncFactory;
import com.huii.auth.config.properties.LoginProperties;
import com.huii.auth.service.LoginProviderService;
import com.huii.common.constants.CacheConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginProviderServiceImpl implements LoginProviderService {

    private final LoginProperties loginProperties;
    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public void checkLoginBans(LoginUser loginUser) {
        if (!loginUser.isEnabled() || !loginUser.isAccountNonLocked() ||
                !loginUser.isAccountNonExpired() || !loginUser.isCredentialsNonExpired()) {
            ResType type = ResType.USER_BANNED;
            sendAsyncMsg(loginUser, SystemConstants.STATUS_0, type.getMessage());
            throw new ServiceException(type.getCode(), ResType.getI18nMessage(type));
        }
    }

    @Override
    public void checkLoginPassword(LoginUser loginUser, String password) {
        if (!SecurityUtils.matchesPassword(password, loginUser.getPassword())) {
            ResType type = ResType.USER_LOGIN_PASSWORD_ERROR;
            sendAsyncMsg(loginUser, SystemConstants.STATUS_0, type.getMessage());
            saveErrorTriedTimes(loginUser.getUsername());
            throw new ServiceException(type.getCode(), ResType.getI18nMessage(type));
        }
    }

    @Override
    public void checkCode(LoginUser loginUser, String key, String code) {
        String cacheCode = redisTemplateUtils.getCacheObject(key);
        if (ObjectUtils.isEmpty(cacheCode)) {
            ResType type = ResType.USER_CAPTCHA_EXPIRED;
            sendAsyncMsg(loginUser, SystemConstants.STATUS_0, type.getMessage());
            throw new ServiceException(type.getCode(), ResType.getI18nMessage(type));
        }
        if (!cacheCode.equals(code)) {
            ResType type = ResType.USER_CAPTCHA_ERROR;
            sendAsyncMsg(loginUser, SystemConstants.STATUS_0, type.getMessage());
            throw new ServiceException(type.getCode(), ResType.getI18nMessage(type));
        }
    }

    @Override
    public void checkLoginSmsCode(LoginUser loginUser, String code) {
        checkCode(loginUser, CacheConstants.VERIFY_CODE + loginUser.getUser().getPhone(), code);
    }

    @Override
    public void checkLoginEmailCode(LoginUser loginUser, String code) {
        checkCode(loginUser, CacheConstants.VERIFY_CODE + loginUser.getUser().getEmail(), code);
    }

    @Override
    public void saveErrorTriedTimes(String username) {
        saveErrorTriedTimes(username, loginProperties.getAuthWaitTime(), loginProperties.getErrorWaitTime());
    }

    @Override
    public void saveErrorTriedTimes(String username, Integer authTime, Integer errorTime) {
        if (loginProperties.getErrorTries() > 0) {
            Integer error = redisTemplateUtils.getCacheObject(CacheConstants.ERROR_TIMES + username);
            if (error == null) {
                redisTemplateUtils.setCacheObject(
                        CacheConstants.ERROR_TIMES + username, 1, errorTime, TimeUnit.MINUTES);
            } else if (error >= loginProperties.getErrorTries()) {
                ResType type = ResType.USER_LOGIN_TRY_MAX;
                throw new ServiceException(
                        type.getCode(), MessageUtils.message(type.getI18n(), loginProperties.getErrorWaitTime()));
            } else {
                redisTemplateUtils.setCacheObject(
                        CacheConstants.ERROR_TIMES + username, error + 1, errorTime, TimeUnit.MINUTES);
            }
        }
        if (loginProperties.getAuthTries() > 0) {
            Integer num = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_TIMES + username);
            if (num == null) {
                redisTemplateUtils.setCacheObject(
                        CacheConstants.VERIFY_TIMES + username, 1, authTime, TimeUnit.MINUTES);
            } else if (num <= loginProperties.getAuthTries()) {
                redisTemplateUtils.setCacheObject(
                        CacheConstants.VERIFY_TIMES + username, num + 1, authTime, TimeUnit.MINUTES);
            }
        }
    }

    @Override
    public void loginSuccessPreHandler(LoginUser loginUser) {
        redisTemplateUtils.deleteObject(CacheConstants.VERIFY_TIMES + loginUser.getUsername());
        redisTemplateUtils.deleteObject(CacheConstants.ERROR_TIMES + loginUser.getUsername());
        sendAsyncMsg(loginUser, SystemConstants.STATUS_1, ResType.USER_LOGIN_SUCCESS.getMessage());
    }

    @Override
    public void sendAsyncMsg(LoginUser loginUser, String status, String msg) {
        AsyncManager.manager().execute(AsyncFactory.loginLogger(
                loginUser.getUsername(), loginUser.getPrinciple(), loginUser.getType().getId(), status, msg));
    }
}

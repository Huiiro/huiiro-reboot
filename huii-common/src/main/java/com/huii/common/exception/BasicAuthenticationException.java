package com.huii.common.exception;


import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
import org.springframework.security.core.AuthenticationException;

/**
 * 基本认证异常
 *
 * @author huii
 */
public class BasicAuthenticationException extends AuthenticationException {
    public BasicAuthenticationException(String message) {
        super(message);
    }

    public BasicAuthenticationException(ResType resType) {
        super(MessageUtils.message(resType.getI18n()));
    }
}

package com.huii.message.exception;

import com.huii.common.exception.ServiceException;

import java.io.Serial;

/**
 * 短信异常
 *
 * @author huii
 */
public class SmsException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SmsException(String msg) {
        super(9001, msg);
    }
}

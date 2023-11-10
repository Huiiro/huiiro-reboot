package com.huii.message.exception;

import com.huii.common.exception.ServiceException;

import java.io.Serial;

public class MailException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MailException(String msg) {
        super(9002, msg);
    }
}

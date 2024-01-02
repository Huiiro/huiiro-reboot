package com.huii.oss.exception;

import com.huii.common.exception.ServiceException;

import java.io.Serial;

public class OssException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(9003, msg);
    }
}

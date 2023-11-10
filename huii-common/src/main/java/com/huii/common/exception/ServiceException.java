package com.huii.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业务异常
 *
 * @author huii
 */
@Getter
@NoArgsConstructor
public class ServiceException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    protected Integer errorCode;
    protected String errorMsg;
    protected String detailErrorMsg;

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public ServiceException(String message) {
        this.errorCode = 500;
        this.errorMsg = message;
    }

    public ServiceException(Integer code, String message) {
        this.errorCode = code;
        this.errorMsg = message;
    }

    public ServiceException(Integer code, String message, String detailErrorMsg) {
        this.errorCode = code;
        this.errorMsg = message;
        this.detailErrorMsg = detailErrorMsg;
    }

    public ServiceException setCode(Integer code) {
        this.errorCode = code;
        return this;
    }

    public ServiceException setMessage(String message) {
        this.errorMsg = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailErrorMsg = detailMessage;
        return this;
    }
}

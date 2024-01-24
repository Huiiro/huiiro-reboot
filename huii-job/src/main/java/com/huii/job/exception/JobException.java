package com.huii.job.exception;

import com.huii.common.exception.ServiceException;

import java.io.Serial;

/**
 * 任务异常
 *
 * @author huii
 */
public class JobException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public JobException(String msg) {
        super(9100, msg);
    }

    public JobException(Integer code, String msg) {
        super(code, msg);
    }
}

package com.huii.generator.exception;

import com.huii.common.exception.ServiceException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 代码生成异常
 *
 * @author huii
 */
@Getter
@NoArgsConstructor
public class GenGeneratorException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GenGeneratorException(String msg) {
        super(9200, msg);
    }

    public GenGeneratorException(Integer code, String msg) {
        super(code, msg);
    }
}

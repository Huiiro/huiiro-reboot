package com.huii.common.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huii
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {

    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = 1000;
    public static final String SUCCESS_MSG = "ok";
    public static final String FAIL_MSG = "fail";
    private Integer code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private static <T> R<T> resultBuilder(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> ok() {
        return resultBuilder(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static <T> R<T> ok(T data) {
        return resultBuilder(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> R<T> ok(String message) {
        return resultBuilder(SUCCESS_CODE, message, null);
    }

    public static <T> R<T> ok(String message, T data) {
        return resultBuilder(SUCCESS_CODE, message, data);
    }

    public static <T> R<T> ok(Integer code, String message) {
        return resultBuilder(code, message, null);
    }

    public static <T> R<T> ok(Integer code, String message, T data) {
        return resultBuilder(code, message, data);
    }

    public static <T> R<T> failed() {
        return resultBuilder(FAIL_CODE, FAIL_MSG, null);
    }

    public static <T> R<T> failed(T data) {
        return resultBuilder(FAIL_CODE, FAIL_MSG, data);
    }

    public static <T> R<T> failed(String message) {
        return resultBuilder(FAIL_CODE, message, null);
    }

    public static <T> R<T> failed(String message, T data) {
        return resultBuilder(FAIL_CODE, message, data);
    }

    public static <T> R<T> failed(Integer code, String message) {
        return resultBuilder(code, message, null);
    }

    public static <T> R<T> failed(Integer code, String message, T data) {
        return resultBuilder(code, message, data);
    }
}
package com.huii.message.core.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传返回体
 *
 * @author huii
 */
@Data
@Builder
public class SmsResult {

    /**
     * 是否成功
     */
    private boolean status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应体
     * <p>
     */
    private String response;
}

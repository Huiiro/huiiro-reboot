package com.huii.message.core.service;

import com.huii.message.core.entity.SmsResult;

import java.util.Map;

/**
 * 短信服务模板接口
 *
 * @author huii
 */
public interface SmsService {

    /**
     * 短信发送模板
     *
     * @param phones   电话号(','分割)
     * @param template 短信模板
     * @param params   模板参数
     *                 <p>
     *                 ali example: key=value
     *                 tencent example: value=key
     *                 </p>
     * @return result
     */
    SmsResult send(String phones, String template, Map<String, String> params);
}

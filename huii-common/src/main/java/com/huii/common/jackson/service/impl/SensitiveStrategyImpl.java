package com.huii.common.jackson.service.impl;


import com.huii.common.utils.DeSensitiveUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏策略实现
 *
 * @author huii
 */
public class SensitiveStrategyImpl {

    /**
     * 邮箱脱敏策略
     */
    public static String desensitizeEmail(String originStr) {
        if (StringUtils.isBlank(originStr)) {
            return "";
        } else {
            int index = StringUtils.indexOf(originStr, '@');
            return index <= 1 ? originStr : DeSensitiveUtils.hideStr(originStr, 1, index);
        }
    }

    /**
     * 手机脱敏策略
     */
    public static String desensitizePhone(String originStr) {
        if (StringUtils.isBlank(originStr)) {
            return "";
        } else {
            return DeSensitiveUtils.hideStr(originStr, 3, originStr.length() - 4);
        }
    }

    /**
     * 身份证脱敏策略
     */
    public static String desensitizeIdCard(String originStr) {
        if (StringUtils.isBlank(originStr)) {
            return "";
        } else {
            return DeSensitiveUtils.hideStr(originStr, 4, originStr.length() - 4);
        }
    }
}

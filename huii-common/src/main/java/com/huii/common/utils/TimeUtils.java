package com.huii.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间处理工具
 *
 * @author huii
 */
public class TimeUtils {

    public static LocalDateTime stringToLocalDateTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }
}

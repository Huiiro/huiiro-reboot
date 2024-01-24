package com.huii.common.core.service;

/**
 * excel 字典数据服务
 *
 * @author huii
 */
public interface DicService {

    String getDicValue(String type, String label, String separator);

    String getDicLabel(String type, String value, String separator);
}

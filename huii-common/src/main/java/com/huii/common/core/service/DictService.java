package com.huii.common.core.service;

public interface DictService {
    String getDictValue(String type, String label, String separator);

    String getDictLabel(String type, String value, String separator);
}

package com.huii.common.core.service;

public interface DicService {
    String getDicValue(String type, String label, String separator);

    String getDicLabel(String type, String value, String separator);
}

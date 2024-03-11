package com.huii.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 前端模板类型
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum VueType {

    VUE_2("1", "vue2", false),
    VUE_3("2", "vue3", true);

    private final String id;
    private final String name;
    private final boolean support;

    public static boolean support(String id) {
        for (VueType value : values()) {
            if (value.id.equals(id)) {
                return value.support;
            }
        }
        return false;
    }
}

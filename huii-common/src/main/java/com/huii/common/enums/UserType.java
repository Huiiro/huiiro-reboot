package com.huii.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 * 根据用户类型判断进行对应操作
 * 提供对应接口，可自行实现逻辑
 *
 * @author huii
 */
@Getter
@AllArgsConstructor
public enum UserType {
    PC,
    MOBILE,
    XSX
}

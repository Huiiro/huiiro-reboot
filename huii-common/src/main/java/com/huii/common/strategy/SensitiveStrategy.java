package com.huii.common.strategy;

import com.huii.common.jackson.service.impl.SensitiveStrategyImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * 脱敏策略枚举类
 *
 * @author huii
 * @see com.huii.common.jackson.service.impl.SensitiveStrategyImpl
 */
@Getter
@AllArgsConstructor
public enum SensitiveStrategy {

    /**
     * 邮箱
     */
    EMAIL("email", SensitiveStrategyImpl::desensitizeEmail),

    /**
     * 电话
     */
    PHONE("phone", SensitiveStrategyImpl::desensitizePhone);

    private final String name;
    private final Function<String, String> desensitized;
}

package com.huii.common.annotation;

import com.huii.common.strategy.LimitStrategy;

import java.lang.annotation.*;

/**
 * 接口限流注解
 *
 * @author huii
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RateLimit {

    /**
     * 最大访问次数
     */
    int access() default 100;

    /**
     * 访问限制时间(单位: s)
     */
    int interval() default 60;

    /**
     * 限流策略
     */
    LimitStrategy strategy() default LimitStrategy.DEFAULT;

    /**
     * 错误提示信息
     */
    String message() default "annotation.rate.limit.default";
}

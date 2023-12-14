package com.huii.common.annotation;

import com.huii.common.enums.OpType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author huii
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Log {

    /**
     * 操作方法描述
     */
    String value() default "";

    /**
     * 操作类型
     */
    OpType opType() default OpType.OTHER;

    /**
     * 存储request参数
     */
    boolean saveRequest() default true;

    /**
     * 存储response参数
     */
    boolean saveResponse() default true;

    /**
     * 参数存储长度
     */
    int maxLength() default 2000;
}

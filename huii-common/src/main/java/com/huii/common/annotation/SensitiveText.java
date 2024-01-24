package com.huii.common.annotation;

import java.lang.annotation.*;

/**
 * 敏感字段过滤注解
 *
 * @author huii
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SensitiveText {

    boolean value() default true;
}

package com.huii.common.annotation;

import com.huii.common.enums.DynamicDataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多数据源注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
    DynamicDataSourceType value() default DynamicDataSourceType.MASTER;
}

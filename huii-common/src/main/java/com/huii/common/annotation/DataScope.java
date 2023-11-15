package com.huii.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 *
 * @author huii
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    /**
     * 部门表的别名
     */
    String deptAlias() default "d";

    /**
     * 用户表的别名
     */
    String userAlias() default "u";
}

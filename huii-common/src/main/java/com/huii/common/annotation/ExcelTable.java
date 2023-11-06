package com.huii.common.annotation;

import java.lang.annotation.*;

/**
 * excel表名
 *
 * @author huii
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelTable {

    /**
     * 表格名称
     */
    String value() default "";
}

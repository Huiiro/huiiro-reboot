package com.huii.common.annotation;

import java.lang.annotation.*;

/**
 * excel字段
 *
 * @author huii
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /**
     * 字段名称
     */
    String value() default "";

    /**
     * 字典名称
     */
    String dictionary() default "";

    /**
     * 数据转换表达式
     * 逗号分割 键值对书写
     * eg. 1=男,2=女,3=未知
     */
    String convert() default "";

    /**
     * 字段是否导出
     */
    boolean export() default true;

    /**
     * TODO 设置样式
     */
}

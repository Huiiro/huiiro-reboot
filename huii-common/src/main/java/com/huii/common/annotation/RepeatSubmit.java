package com.huii.common.annotation;

import java.lang.annotation.*;

/**
 * 防止表单重复提交
 *
 * @author huii
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatSubmit {

    /**
     * 间隔时间(单位: ms)
     */
    int interval() default 5000;

    /**
     * 错误提示信息
     */
    String message() default "annotation.repeat.submit.default";
}

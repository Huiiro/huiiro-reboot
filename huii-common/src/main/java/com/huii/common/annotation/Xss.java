package com.huii.common.annotation;

import com.huii.common.validator.XssValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Xss校验注解
 *
 * @author huii
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {XssValidator.class})
public @interface Xss {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "annotation.xss.default";
}

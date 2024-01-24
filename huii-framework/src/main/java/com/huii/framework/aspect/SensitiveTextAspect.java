package com.huii.framework.aspect;

import com.huii.common.annotation.SensitiveText;
import com.huii.common.utils.SensitiveTextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 敏感词过滤切面
 *
 * @author huii
 */
@Slf4j
@Aspect
@Component
public class SensitiveTextAspect {

    @Pointcut("@within(com.huii.common.annotation.SensitiveText) || " +
            "@annotation(com.huii.common.annotation.SensitiveText)")
    public void sensitiveTextPointCut() {
    }

    @Around("sensitiveTextPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        boolean enableFilter;
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        SensitiveText methodSensitiveWordFilter = method.getAnnotation(SensitiveText.class);
        SensitiveText clazzSensitiveWordFilter = clazz.getAnnotation(SensitiveText.class);
        if (methodSensitiveWordFilter != null) {
            enableFilter = methodSensitiveWordFilter.value();
        } else {
            enableFilter = clazzSensitiveWordFilter.value();
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramValues = point.getArgs();
        if (enableFilter) {
            for (int i = 0; i < paramValues.length; i++) {
                Object value = paramValues[i];
                if (parameterTypes[i].isAssignableFrom(String.class)) {
                    if (null != value) {
                        value = SensitiveTextUtils.replaceSensitiveWord(
                                (String) value, '*', SensitiveTextUtils.MinMatchTYpe);
                    }
                } else if (!isBasicType(parameterTypes[i])) {
                    Field[] fields = value.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        Class<?> type = field.getType();
                        if (type.isAssignableFrom(String.class)) {
                            field.setAccessible(true);
                            String fieldValue = (String) field.get(value);
                            if (null != fieldValue) {
                                fieldValue = SensitiveTextUtils.replaceSensitiveWord(
                                        fieldValue, '*', SensitiveTextUtils.MinMatchTYpe);
                                field.set(value, fieldValue);
                            }
                        }
                    }
                }
                paramValues[i] = value;
            }
        }
        return point.proceed(paramValues);
    }

    /**
     * 判断基本类型
     *
     * @param clazz clazz
     * @return boolean
     */
    private boolean isBasicType(Class<?> clazz) {
        return clazz.isAssignableFrom(Integer.class) ||
                clazz.isAssignableFrom(Byte.class) ||
                clazz.isAssignableFrom(Long.class) ||
                clazz.isAssignableFrom(Double.class) ||
                clazz.isAssignableFrom(Float.class) ||
                clazz.isAssignableFrom(Character.class) ||
                clazz.isAssignableFrom(Short.class) ||
                clazz.isAssignableFrom(Boolean.class);
    }
}

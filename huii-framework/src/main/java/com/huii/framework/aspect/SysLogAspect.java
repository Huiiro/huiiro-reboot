package com.huii.framework.aspect;

import com.alibaba.fastjson2.JSON;
import com.huii.async.factory.AsyncFactory;
import com.huii.async.manager.AsyncManager;
import com.huii.common.annotation.Log;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.utils.SecurityUtils;
import com.huii.common.utils.request.IpAddressUtils;
import com.huii.system.domain.SysLogOp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class SysLogAspect {

    private static final int maxLength = 2000;
    private static final ThreadLocal<Long> LOG_ASPECT_THREADLOCAL = new NamedThreadLocal<>("log-aspect-thread-local");

    @Pointcut("@annotation(com.huii.common.annotation.Log)")
    public void logPointCut() {
    }

    @Before(value = "logPointCut()")
    public void boBefore() {
        LOG_ASPECT_THREADLOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(value = "logPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        handleLog(joinPoint, exception, null);
    }

    private void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
        try {
            Log log = getAnnotationSystemLog(joinPoint);
            if (log == null) {
                return;
            }
            String username = "anonymousUser";
            SysUser user = SecurityUtils.getUser();
            if (user != null) {
                username = user.getUserName();
            }

            HttpServletRequest request = getHttpServletRequest();
            String ip = IpAddressUtils.getIp(request);
            String address = IpAddressUtils.getAddr(ip);
            String requestMethod = request.getMethod();
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getName();

            String requestParams = getRequestParams(log, joinPoint, requestMethod);
            String responseParams = getResponseParams(log, result);

            long costTIme = System.currentTimeMillis() - LOG_ASPECT_THREADLOCAL.get();
            String opStatus = SystemConstants.STATUS_1;
            String opMessage = "";
            if (ObjectUtils.isNotEmpty(e)) {
                opStatus = SystemConstants.STATUS_0;
                opMessage = e.getMessage().length() > maxLength ?
                        e.getMessage().substring(0, maxLength) : e.getMessage();
            }

            SysLogOp op = new SysLogOp(null, username, methodName, log.opType().getId(), LocalDateTime.now(), costTIme,
                    ip, address, requestMethod, methodName + className, requestParams, responseParams,
                    opStatus, "0", opMessage, null);
            AsyncManager.manager().execute(AsyncFactory.apiLogger(op));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            LOG_ASPECT_THREADLOCAL.remove();
        }
    }

    private String getRequestParams(Log log, JoinPoint joinPoint, String method) {
        if (log.saveRequest() && (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method))) {
            String requestParam = argsArrayToString(joinPoint.getArgs());
            if (StringUtils.isNotEmpty(requestParam) && requestParam.length() > maxLength) {
                return requestParam.substring(0, maxLength);
            } else {
                return requestParam;
            }
        }
        return null;
    }

    private String getResponseParams(Log log, Object result) {
        if (log.saveResponse()) {
            String responseParam = JSON.toJSONString(result);
            if (StringUtils.isNotEmpty(responseParam) && responseParam.length() > maxLength) {
                return responseParam.substring(0, maxLength);
            } else {
                return responseParam;
            }
        }
        return null;
    }

    /**
     * 获取注解
     *
     * @param joinPoint joinPoint
     * @return annotation
     */
    private Log getAnnotationSystemLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method != null ? method.getAnnotation(Log.class) : null;
    }

    /**
     * 获取request对象
     *
     * @return HttpServletRequest.request
     */
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
        assert servletRequestAttributes != null;
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取请求参数
     *
     * @param paramsArray Object[]
     * @return String params
     */
    public static String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (!isFilterObject(o)) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params.append(jsonObj.toString()).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    public static boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}

package com.huii.framework.exception;

import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author huii
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * AuthenticationException
     * 认证校验异常 401
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(AuthenticationException.class)
    public R<Object> authenticationExceptionHandler(AuthenticationException e, HttpServletRequest request) {
        log.error("AuthenticationException:请求地址'{}',认证失败'{}' =>", request.getRequestURI(), e.getMessage());
        String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : ResType.getI18nMessage(ResType.STATUS_UNAUTHORIZED);
        return R.failed(ResType.STATUS_UNAUTHORIZED.getCode(), message);
    }

    /**
     * AccessDeniedException
     * 权限校验异常 403
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<Object> accessDeniedExceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        log.error("AccessDeniedException:请求地址'{}',权限校验失败'{}' =>", request.getRequestURI(), e.getMessage());
        String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : ResType.getI18nMessage(ResType.STATUS_FORBIDDEN);
        return R.failed(ResType.STATUS_FORBIDDEN.getCode(), message);
    }

    /**
     * ServiceException
     * 自定义业务异常 <code>
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(value = ServiceException.class)
    public R<Object> serviceExceptionHandler(ServiceException e, HttpServletRequest request) {
        log.error("ServiceException:请求地址:'{}',返回信息:'{}',业务异常:'{}' =>", request.getRequestURI(), e.toString(), e.getMessage());
        return R.failed(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * RuntimeException
     * 运行时异常 500
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(value = RuntimeException.class)
    public R<Object> runtimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        log.error("RuntimeException:请求地址'{}',运行异常'{}' =>", request.getRequestURI(), e.getMessage());
        return R.failed(500, e.getMessage());
    }

    /**
     * NullPointerException
     * 空指针异常 500
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(value = NullPointerException.class)
    public R<Object> nullPointerExceptionHandler(NullPointerException e, HttpServletRequest request) {
        log.error("NullPointerException:请求地址'{}',空指针异常'{}' =>", request.getRequestURI(), e.getMessage(), e);
        return R.failed(9999,"系统异常，请联系管理员");
    }

    /**
     * Exception
     * 异常 500 / 1000
     *
     * @param e       e
     * @param request request
     * @return r
     */
    @ExceptionHandler(value = Exception.class)
    public R<Object> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("Exception:请求地址:'{}',系统异常:'{}' =>", request.getRequestURI(), e.getMessage(), e);
        return R.failed(1000,"系统异常，请稍后尝试");
    }
}

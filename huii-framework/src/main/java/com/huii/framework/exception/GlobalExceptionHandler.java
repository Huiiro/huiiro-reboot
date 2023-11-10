package com.huii.framework.exception;

import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
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
}

package com.huii.auth.handler;

import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.exception.BasicAuthenticationException;
import com.huii.common.utils.JsonWriteUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 未认证处理器
 * http_status: 401
 *
 * @author huii
 */
@Slf4j
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        R<Object> result = R.failed(ResType.STATUS_UNAUTHORIZED.getCode(), ResType.getI18nMessage(ResType.STATUS_UNAUTHORIZED), null);
        if (exception instanceof BasicAuthenticationException) {
            result.setMessage(exception.getMessage());
        }
        JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_UNAUTHORIZED, result);
    }
}

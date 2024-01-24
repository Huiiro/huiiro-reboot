package com.huii.auth.handler;

import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.exception.BasicAuthenticationException;
import com.huii.common.utils.JsonWriteUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
        int status = response.getStatus();
        R<Object> result;
        ResType type;
        if (ObjectUtils.isNotEmpty(request.getAttribute("tokenException"))) {
            //token验证失效
            status = HttpServletResponse.SC_UNAUTHORIZED;
            type = ResType.STATUS_TOKEN_EXPIRED;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
        } else if (status == 404) {
            //请求地址不存在
            type = ResType.STATUS_NOT_FOUND;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
        } else if (status == 405) {
            //请求方式错误
            type = ResType.STATUS_METHOD_NOT_ALLOWED;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
        } else if (status == 408) {
            //请求超时
            type = ResType.STATUS_REQUEST_TIMEOUT;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
        } else if (status == 429) {
            //请求限流
            type = ResType.STATUS_TOO_MANY_REQUESTS;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
        } else {
            //其他异常
            status = HttpServletResponse.SC_UNAUTHORIZED;
            type = ResType.STATUS_UNAUTHORIZED;
            result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
            if (exception instanceof BasicAuthenticationException) {
                result.setMessage(exception.getMessage());
            }
        }
        JsonWriteUtils.writeOptJson(response, status, result);
    }
}

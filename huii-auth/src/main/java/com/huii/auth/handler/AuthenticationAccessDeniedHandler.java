package com.huii.auth.handler;

import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.utils.JsonWriteUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 未授权处理器
 * http_status: 403
 *
 * @author huii
 */
@Slf4j
@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        R<Object> result = R.failed(ResType.STATUS_FORBIDDEN.getCode(), ResType.getI18nMessage(ResType.STATUS_FORBIDDEN), null);
        JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_FORBIDDEN, result);
    }
}

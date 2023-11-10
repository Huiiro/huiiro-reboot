package com.huii.auth.handler;

import com.huii.auth.config.properties.JwtProperties;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.utils.JsonWriteUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 登出成功处理器
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler implements
        org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final RedisTemplateUtils redisTemplateUtils;
    private final JwtProperties jwtProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (ObjectUtils.isNotEmpty(authentication)) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getUserId();
            redisTemplateUtils.deleteObject(CacheConstants.USER + id);
            redisTemplateUtils.deleteObject(CacheConstants.TOKEN + id);
            if (Objects.equals(jwtProperties.getEnableDoubleToken(), Boolean.TRUE.toString())) {
                redisTemplateUtils.deleteObject(CacheConstants.REFRESH + id);
            }
        }
        R<Object> result = R.ok(ResType.getI18nMessage(ResType.USER_LOGOUT_SUCCESS), null);
        JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_OK, result);
    }
}

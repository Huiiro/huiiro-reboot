package com.huii.framework.handler;

import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.utils.JsonWriteUtils;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.IpAddressUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.*;

/**
 * IP拦截器
 *
 * @author huii
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ForbiddenIpInterceptor implements HandlerInterceptor {

    private final RedisTemplateUtils redisTemplateUtils;
    private final Set<String> ips = new HashSet<>();

    @PostConstruct
    public void init() {
        String cacheObject = redisTemplateUtils.getCacheObject(CacheConstants.SYS_CONFIG + "sys_black_list");
        if (cacheObject != null) {
            String[] split = cacheObject.split(",");
            Collections.addAll(ips, split);
        }
        log.debug("ForbiddenIpInterceptor config load forbidden ips: {}", ips.size());
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String ipAddress = IpAddressUtils.getIp(request);
        if (!ips.isEmpty() && StringUtils.isNotEmpty(ipAddress)) {
            for (String s : ips) {
                if (s.equals(ipAddress)) {
                    ResType rs = ResType.USER_NOT_ALLOW_IP;
                    R<Object> result = R.failed(rs.getCode(), MessageUtils.message(rs.getI18n()));
                    JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_FORBIDDEN, result);
                    return false;
                }
            }
        }
        return true;
    }
}
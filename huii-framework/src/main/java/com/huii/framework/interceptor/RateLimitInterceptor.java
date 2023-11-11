package com.huii.framework.interceptor;

import com.huii.common.annotation.RateLimit;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.R;
import com.huii.common.strategy.LimitStrategy;
import com.huii.common.utils.JsonWriteUtils;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.IpAddressUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * 限流拦截器
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            RateLimit limit = handlerMethod.getMethodAnnotation(RateLimit.class);
            if (limit != null) {
                LimitStrategy strategy = limit.strategy();
                if(strategy.equals(LimitStrategy.DEFAULT)) {
                    //默认限流策略
                    String key = CacheConstants.RATE_LIMIT + IpAddressUtils.getIp(request) + ":" + request.getRequestURI();
                    if (ObjectUtils.isEmpty(redisTemplateUtils.getCacheObject(key))) {
                        int second = limit.interval();
                        redisTemplateUtils.setCacheObject(key, "1:" + second + ":" + System.currentTimeMillis(), second, TimeUnit.SECONDS);
                        return true;
                    }
                    int number = limit.access();
                    String keyValue = redisTemplateUtils.getCacheObject(key);
                    String[] parts = keyValue.split(":");
                    if (Integer.parseInt(parts[0]) >= number) {
                        R<String> result = R.failed(MessageUtils.message(limit.message()));
                        JsonWriteUtils.writeOptJson(response, 429, result);
                        return false;
                    }
                    long cur = System.currentTimeMillis();
                    int restTime = (int) (Integer.parseInt(parts[1]) - Math.abs((Long.parseLong(parts[2]) - cur) / 1000));
                    String val = (parts[0] + 1) + ":" + restTime + ":" + cur;
                    redisTemplateUtils.setCacheObject(key, val, restTime, TimeUnit.SECONDS);
                } else if (strategy.equals(LimitStrategy.BUCKET)) {
                    //令牌桶策略
                }else if (strategy.equals(LimitStrategy.WINDOW)) {
                    //滑动窗口策略
                }
            }
        }
        return true;
    }
}
package com.huii.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.filter.wrapper.RepeatedlyRequestWrapper;
import com.huii.common.utils.JsonWriteUtils;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.common.utils.request.HttpBodyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交拦截器
 *
 * @author ruoyi
 */
@Component
@RequiredArgsConstructor
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    public final String REPEAT_PARAMS = "repeatParams";
    public final String REPEAT_TIME = "repeatTime";

    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            RepeatSubmit annotation = handlerMethod.getMethodAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    return printErrorInfo(response, annotation.message());
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper repeatedlyRequest) {
            nowParams = HttpBodyUtils.getBodyString(repeatedlyRequest);
        }
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<>(2);
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        String url = request.getRequestURI();
        String submitKey = StringUtils.trimToEmpty(request.getHeader("token"));
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT + url + submitKey;

        Object cache = redisTemplateUtils.getCacheObject(cacheRepeatKey);
        if (cache != null) {
            Map<String, Object> redisMap = (Map<String, Object>) cache;
            if (redisMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) redisMap.get(url);
                if (compareParams(nowDataMap, preDataMap) &&
                        compareTime(nowDataMap, preDataMap, annotation.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<>(1);
        cacheMap.put(url, nowDataMap);
        redisTemplateUtils.setCacheObject(cacheRepeatKey, cacheMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }

    private boolean printErrorInfo(HttpServletResponse response, String msg) {
        R<String> result = R.failed(ResType.ANNOTATION_REPEAT_SUBMIT_DEFAULT.getCode(),
                MessageUtils.message(msg));
        JsonWriteUtils.writeOptJson(response, 200, result);
        return false;
    }
}

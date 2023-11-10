package com.huii.auth.factory;

import com.huii.auth.strategy.LoginStrategy;
import com.huii.common.enums.LoginType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录策略工厂类
 *
 * @author huii
 */
public class LoginStrategyFactory {

    private static final Map<String, LoginStrategy> map = new HashMap<>();

    /**
     * 注册策略
     */
    public static void register(String key, LoginStrategy loginStrategy) {
        if (StringUtils.isEmpty(key) && null == loginStrategy) {
            return;
        }
        map.put(key, loginStrategy);
    }

    /**
     * 获取策略
     */
    public static LoginStrategy getStrategy(String key) {
        return map.get(key);
    }

    public static LoginStrategy getStrategy(LoginType type) {
        return getStrategy(type.getKey());
    }
}

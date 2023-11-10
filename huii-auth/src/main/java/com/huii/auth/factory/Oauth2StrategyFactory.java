package com.huii.auth.factory;

import com.huii.auth.oauth2.Oauth2Login;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Oauth2登录策略工厂类
 *
 * @author huii
 */
public class Oauth2StrategyFactory {

    private static final Map<String, Oauth2Login> map = new HashMap<>();

    public static void register(String key, Oauth2Login oauthStrategy) {
        if (StringUtils.isEmpty(key) && null == oauthStrategy) {
            return;
        }
        map.put(key, oauthStrategy);
    }

    public static Oauth2Login getStrategy(String key) {
        return map.get(key);
    }
}

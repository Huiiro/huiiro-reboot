package com.huii.auth.factory;

import com.huii.auth.strategy.Oauth2Login;
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

    /**
     * to register all the oauth2 type
     *
     * @param key           key
     * @param oauthStrategy oauthStrategy
     * @see com.huii.auth.strategy.login.Oauth2LoginStrategy
     */
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

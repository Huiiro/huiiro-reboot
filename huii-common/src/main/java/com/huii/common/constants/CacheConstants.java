package com.huii.common.constants;

public interface CacheConstants {

    /**
     * 用户信息
     */
    String USER = "auth_user:";

    /**
     * accessToken
     */
    String TOKEN = "auth_token:";

    /**
     * refreshToken
     */
    String REFRESH = "auth_refresh:";

    /**
     * 登录验证码
     */
    String VERIFY_CODE = "verify_code:";

    /**
     * 登录验证次数
     */
    String VERIFY_TIMES = "verify_try_times:";

    /**
     * 登录错误次数
     */
    String ERROR_TIMES = "verify_error_times:";
}

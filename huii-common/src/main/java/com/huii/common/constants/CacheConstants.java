package com.huii.common.constants;

/**
 * @author huii
 */
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
     * 登录验证码
     */
    String VERIFY_CODE = "verify_code:";

    String VERIFY_CODE_RESET_PWD_SUFFIX = VERIFY_CODE + "reset_pwd:";

    /**
     * 登录验证次数
     */
    String VERIFY_TIMES = "verify_try_times:";

    /**
     * 登录错误次数
     */
    String ERROR_TIMES = "verify_error_times:";

    /**
     * 防止重复提交
     */
    String REPEAT_SUBMIT = "repeat_submit:";

    /**
     * 接口限流
     */
    String RATE_LIMIT = "rate_limit:";

    /**
     * 系统配置
     */
    String SYS_CONFIG = "sys_config:";

    /**
     * 系统字典
     */
    String SYS_DICT = "sys_dict:";

    /**
     * 默认缓存时间，10min
     */
    Integer DEFAULT_CACHE_TIME = 10;
}

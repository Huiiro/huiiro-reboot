package com.huii.auth.service;

import com.huii.common.core.model.LoginUser;

public interface LoginProviderService {

    /**
     * 校验用户是否封禁
     * 用户已通过登录校验
     *
     * @param loginUser LoginUser
     */
    void checkLoginBans(LoginUser loginUser);

    /**
     * 校验 <password> 登录密码
     *
     * @param loginUser LoginUser
     * @param password  password
     */
    void checkLoginPassword(LoginUser loginUser, String password);

    /**
     * 校验登录验证码 redis key cache code
     *
     * @param loginUser loginUser
     * @param key       redisCache
     * @param code      user input code
     */
    void checkCode(LoginUser loginUser, String key, String code);

    /**
     * 校验 <sms code> 登录验证码
     *
     * @param loginUser LoginUser
     * @param code      code
     */
    void checkLoginSmsCode(LoginUser loginUser, String code);

    /**
     * 校验 <email code> 登录验证码
     *
     * @param loginUser LoginUser
     * @param code      code
     */
    void checkLoginEmailCode(LoginUser loginUser, String code);

    /**
     * 保存登录错误次数
     *
     * @param username  username
     */
    void saveErrorTriedTimes(String username);

    /**
     * 保存登录错误次数
     *
     * @param username  username
     * @param authTime  minutes default 0.5d
     * @param errorTime minutes default 10min
     */
    void saveErrorTriedTimes(String username, Integer authTime, Integer errorTime);

    /**
     * 登录成功前置处理器
     *
     * @param loginUser loginUser
     */
    void loginSuccessPreHandler(LoginUser loginUser);

    /**
     * 发送登录请求处理结果异步请求
     *
     * @param loginUser LoginUser
     * @param status    status
     * @param msg       msg
     */
    void sendAsyncMsg(LoginUser loginUser, Integer status, String msg);
}

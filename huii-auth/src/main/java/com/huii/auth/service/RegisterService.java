package com.huii.auth.service;

import com.huii.auth.core.entity.RegisterEntity;

public interface RegisterService {

    /**
     * 校验用户名是否重复
     *
     * @param username username
     * @return Boolean
     */
    Boolean checkUsername(String username);

    /**
     * 注册方法
     *
     * @param entity RegisterEntity
     */
    void register(RegisterEntity entity);
}

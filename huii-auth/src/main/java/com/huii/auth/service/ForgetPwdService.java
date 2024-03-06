package com.huii.auth.service;

import com.huii.common.core.domain.SysUser;

public interface ForgetPwdService {
    SysUser getUser(String username);
}

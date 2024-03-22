package com.huii.auth.core.service;

import com.huii.common.core.domain.SysUser;

public interface ForgetPwdService {
    SysUser getUser(String username);
}

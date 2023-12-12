package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysLogLogin;

import java.util.List;

public interface SysLogLoginService {
    Page selectSysLogLoginList(SysLogLogin sysLogLogin, PageParam pageParam);

    List<SysLogLogin> selectSysLogLoginList(SysLogLogin sysLogLogin);

    void removeBatchByIds(Long[] ids);

    void removeAll();
}

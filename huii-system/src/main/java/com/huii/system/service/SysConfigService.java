package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysConfig;

public interface SysConfigService {
    void clearCache();
    void load();
    void refreshCache();
    Page selectConfigList(SysConfig sysConfig, PageParam pageParam);

    SysConfig selectConfigById(Long id);

    void checkInsert(SysConfig sysConfig);

    void insertConfig(SysConfig sysConfig);

    void checkUpdate(SysConfig sysConfig);

    void updateConfig(SysConfig sysConfig);

    void deleteConfig(Long[] ids);
}

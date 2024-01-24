package com.huii.system.service;

import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.system.domain.SysConfig;

public interface SysConfigService {

    /**
     * 清除缓存
     */
    void clearCache();

    /**
     * 加载配置
     */
    void load();

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 查询配置集合
     *
     * @param sysConfig sysConfig
     * @param pageParam pageParam
     * @return list
     */
    Page selectConfigList(SysConfig sysConfig, PageParam pageParam);

    /**
     * 查询配置
     *
     * @param id id
     * @return sysConfig
     */
    SysConfig selectConfigById(Long id);

    /**
     * 校验插入数据
     *
     * @param sysConfig sysConfig
     */
    void checkInsert(SysConfig sysConfig);

    /**
     * 添加配置
     *
     * @param sysConfig sysConfig
     */
    void insertConfig(SysConfig sysConfig);

    /**
     * 校验修改数据
     *
     * @param sysConfig sysConfig
     */
    void checkUpdate(SysConfig sysConfig);

    /**
     * 修改配置
     *
     * @param sysConfig sysConfig
     */
    void updateConfig(SysConfig sysConfig);

    /**
     * 删除配置
     *
     * @param ids ids
     */
    void deleteConfig(Long[] ids);
}

package com.huii.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统 缓存 实体类
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysCache {

    private String cacheName = "";

    private String cacheKey = "";

    private String cacheValue = "";

    private String cacheRemark = "";

    public SysCache(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public SysCache(String cacheName, String cacheRemark) {
        this.cacheName = cacheName;
        this.cacheRemark = cacheRemark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = cacheName;
        this.cacheKey = cacheKey;
        this.cacheValue = cacheValue;
    }
}
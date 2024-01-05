package com.huii.controller.monitor;

import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.system.domain.SysCache;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Validated
@RestController
@RequestMapping("/system/cache")
@RequiredArgsConstructor
public class SysCacheController extends BaseController {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisConnectionFactory redissonConnectionFactory;
    private final static List<SysCache> CACHES = new ArrayList<>();

    static {
        CACHES.add(new SysCache(CacheConstants.USER, "用户信息"));
        CACHES.add(new SysCache(CacheConstants.TOKEN, "用户凭证"));
        CACHES.add(new SysCache(CacheConstants.VERIFY_CODE, "验证码信息"));
        CACHES.add(new SysCache(CacheConstants.VERIFY_TIMES, "验证码错误次数"));
        CACHES.add(new SysCache(CacheConstants.ERROR_TIMES, "登录错误次数"));
        CACHES.add(new SysCache(CacheConstants.RATE_LIMIT, "接口限流"));
        CACHES.add(new SysCache(CacheConstants.REPEAT_SUBMIT, "重复提交"));
        CACHES.add(new SysCache(CacheConstants.SYS_DICT, "系统字典"));
        CACHES.add(new SysCache(CacheConstants.SYS_CONFIG, "系统参数"));
    }

    @SuppressWarnings("all")
    @GetMapping("/info")
    public R<Map<String, Object>> getCacheInfo() {
        RedisConnection connection = redissonConnectionFactory.getConnection();
        Properties commandStats = connection.info("commandstats");
        RedisServerCommands commands = connection.serverCommands();
        Properties properties = commands.info();
        Long dbSize = commands.dbSize();
        Map<String, Object> map = new HashMap<>(3);
        List<Map<String, String>> list = new ArrayList<>();
        Objects.requireNonNull(commandStats).stringPropertyNames().forEach(k -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(k);
            data.put("name", StringUtils.removeStart(k, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            list.add(data);
        });
        map.put("properties", properties);
        map.put("size", dbSize);
        map.put("commands", list);
        return R.ok(map);
    }

    /**
     * 获取缓存集合
     */
    @GetMapping("/list")
    public R<List<SysCache>> getCaches() {
        return R.ok(CACHES);
    }

    /**
     * 获取缓存键值
     */
    @GetMapping("/key/{cacheName}")
    public R<Set<String>> getCacheKey(@PathVariable String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + "*");
        return R.ok(keys);
    }

    /**
     * 获取缓存值
     */
    @GetMapping("/key/value/{cacheKey}")
    public R<SysCache> getCacheKeyValue(@PathVariable String cacheKey) {
        try {
            String value = redisTemplate.opsForValue().get(cacheKey);
            SysCache sysCache = new SysCache(null, cacheKey, value, null);
            return R.ok(sysCache);
        } catch (Exception e) {
            return R.failed("暂不支持查看");
        }
    }

    /**
     * 删除缓存值
     */
    @PostMapping("/delete/value/{cacheKey}")
    public R<Object> clearCacheValue(@PathVariable String cacheKey) {
        redisTemplate.delete(cacheKey);
        return deleteSuccess();
    }

    /**
     * 删除缓存键值
     */
    @PostMapping("/delete/key/{cacheKey}")
    public R<Void> clearCacheKey(@PathVariable String cacheKey) {
        Collection<String> keys = redisTemplate.keys(cacheKey + "*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        return deleteSuccess();
    }

    /**
     * 清空缓存
     */
    @PostMapping("/delete/all")
    public R<Object> clearCaches() {
        Collection<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        return deleteSuccess();
    }
}

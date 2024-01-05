import request from '@/utils/request.ts';

const prefix = "/system/cache";

enum API {
    GET_INFO = prefix + "/info",
    GET_LIST = prefix + "/list",
    GET_KEY = prefix + "/key/",
    GET_KEY_VALUE = prefix + "/key/value/",
    DELETE_KEY = prefix + "/delete/key/",
    DELETE_VALUE = prefix + "/delete/value/",
    DELETE_ALL = prefix + "/delete/all",
}

/**
 * 获取缓存集合
 */
export const getCacheInfo = () => request.get(API.GET_INFO);

/**
 * 获取缓存集合
 */
export const getCacheList = () => request.get(API.GET_LIST);

/**
 * 获取缓存键值
 */
export const getCacheKey = (cacheName: string) => request.get(API.GET_KEY + cacheName);

/**
 * 获取缓存值
 */
export const getCacheKeyValue = (cacheKey: string) => request.get(API.GET_KEY_VALUE + cacheKey);

/**
 * 删除缓存键值
 */
export const deleteCacheKey = (cacheKey: string) => request.post(API.DELETE_KEY + cacheKey);

/**
 * 删除缓存值
 */
export const deleteCacheValue = (cacheKey: string) => request.post(API.DELETE_VALUE + cacheKey);

/**
 * 删除缓存值
 */
export const deleteCaches = () => request.post(API.DELETE_ALL);
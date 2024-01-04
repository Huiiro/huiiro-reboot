import request from '@/utils/request.ts';

const prefix = "/system/config";

export interface config {
    configId: number
    configName: string
    configKey: string
    configValue: string
    configRemark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    REFRESH = prefix + "/refresh",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取系统配置
 */
export const refreshConfig = () => request.get(API.REFRESH);

/**
 * 获取系统配置
 */
export const getConfigList = (config: config) => request.get(API.GET_LIST, {params: config});

/**
 * 获取单个系统配置
 */
export const getConfigSingleton = (configId: number) => request.get(API.GET_ONE + configId);

/**
 * 添加系统配置
 */
export const insertConfig = (config: config) => request.post(API.INSERT_ONE, config);

/**
 * 更新系统配置
 */
export const updateConfig = (config: config) => request.post(API.UPDATE_ONE, config);

/**
 * 删除系统配置
 */
export const deleteConfig = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);
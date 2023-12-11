/**
 * API Template
 * @author huii
 */
let var1 = "user"
let var2 = "User"
let var3 = "用户"

import request from '@/utils/request.ts';

const prefix = "/system/var1";

export interface var1 {
    var1Name: string
}

enum API {
    EXPORT = prefix + "/export",
    IMPORT = prefix + "/import",
    IMPORT_TEMPLATE_DOWN = prefix + "/import/template/down",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取var3列表
 */
export const getvar2List = (var1: var1) => request.get(API.GET_LIST, {params: var1});

/**
 * 获取单个var3
 */
export const getvar2Singleton = (var1Id: number) => request.get(API.GET_ONE + var1Id);

/**
 * 添加var3
 */
export const insertvar2 = (var1: var1) => request.post(API.INSERT_ONE, var1);

/**
 * 更新var3
 */
export const updatevar2 = (var1: var1) => request.post(API.UPDATE_ONE, var1);

/**
 * 删除var3
 */
export const deletevar2 = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

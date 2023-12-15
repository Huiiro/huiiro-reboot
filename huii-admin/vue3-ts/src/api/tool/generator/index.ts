import request from '@/utils/request.ts';

const prefix = "/gen";

export interface genTable {
    genTableName: string
}

enum API {
    GET_DB_LIST = prefix + "/list/db",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    GET_BY_NAME = prefix + "/name",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取数据库表格列表
 */
export const getGenTableDbList = (genTable: genTable) => request.get(API.GET_DB_LIST, {params: genTable});

/**
 * 获取表格列表
 */
export const getGenTableList = (genTable: genTable) => request.get(API.GET_LIST, {params: genTable});

/**
 * 获取单个表格
 */
export const getGenTableSingleton = (genTableId: number) => request.get(API.GET_ONE + genTableId);

/**
 * 获取单个表格
 */
export const getGenTableSingletonByName = (genTableName: string) => request.get(API.GET_BY_NAME,
    {params: {'tableName': genTableName}});

/**
 * 添加表格 保存 db 表至 table 表
 */
export const insertGenTable = (ids: Array<number>) => request.post(API.INSERT_ONE, ids);

/**
 * 更新表格
 */
export const updateGenTable = (genTable: genTable) => request.post(API.UPDATE_ONE, genTable);

/**
 * 删除表格
 */
export const deleteGenTable = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

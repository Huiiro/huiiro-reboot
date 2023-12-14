import request from '@/utils/request.ts';

const prefix = "/system/dic/data";

export interface dicData {
    dataId: number
    dataType: string
    dataName: string
    dataKey: string
    dataValue: string
    dataLabel: string
    dataSeq: string
    dataDefault: string
    dataStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    EXPORT = prefix + "/export",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取字典数据
 */
export const getDicDataList = (dicData: dicData) => request.get(API.GET_LIST, {params: dicData});

/**
 * 获取单个字典数据
 */
export const getDicDataSingleton = (dataId: number) => request.get(API.GET_ONE + dataId);

/**
 * 添加字典数据
 */
export const insertDicData = (dicData: dicData) => request.post(API.INSERT_ONE, dicData);

/**
 * 更新字典数据
 */
export const updateDicData = (dicData: dicData) => request.post(API.UPDATE_ONE, dicData);

/**
 * 删除字典数据
 */
export const deleteDicData = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 导出字典数据
 */
export const exportDicData = (dicData: dicData | null) => request.get(API.EXPORT, {responseType: 'blob', data: dicData});
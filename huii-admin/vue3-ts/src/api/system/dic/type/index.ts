import request from '@/utils/request.ts';

const prefix = "/system/dic/type";

export interface dicType {
    typeId: number
    typeName: string
    dicType: string
    typeStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    EXPORT = prefix + "/export",
    REFRESH = prefix + "/refresh",
    GET_LIST = prefix + "/list",
    GET_LABEL = prefix + "/label",
    GET_ONE = prefix + "/",
    GET_BY_NAME = "/type",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 手动刷新字典缓存
 */
export const refreshDicType = () => request.get(API.REFRESH);

/**
 * 获取字典类型
 */
export const getDicTypeList = (dicType: dicType) => request.get(API.GET_LIST, {params: dicType});

/**
 * 获取字典类型
 */
export const getDicTypeLabel = () => request.get(API.GET_LABEL);

/**
 * 获取单个字典类型
 */
export const getDicTypeSingleton = (typeId: number) => request.get(API.GET_ONE + typeId);

/**
 * 获取单个字典类型
 */
export const getDicTypeSingletonByTypeName = (dicType: string) => request.get(API.GET_BY_NAME, {params: dicType});

/**
 * 添加字典类型
 */
export const insertDicType = (dicType: dicType) => request.post(API.INSERT_ONE, dicType);

/**
 * 更新字典类型
 */
export const updateDicType = (dicType: dicType) => request.post(API.UPDATE_ONE, dicType);

/**
 * 删除字典类型
 */
export const deleteDicType = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 导出字典类型
 */
export const exportDicType = (dicType: dicType | null) => request.get(API.EXPORT, {responseType: 'blob', data: dicType});
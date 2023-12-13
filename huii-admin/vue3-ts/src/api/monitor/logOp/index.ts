import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/log/op";

export interface logOp {
    opId: number
    opUserName: string
    opMethodMame: string
    opType: number
    opTime: string
    opCostTime: string
    opIp: string
    opAddress: string
    opRequest: string
    opUrl: string
    opReqParam: string
    opResParam: string
    opStatus: string
    opMarkFlag: string
    opMessage: string
}

enum API {
    GET_LIST = prefix + "/list",
    EXPORT = prefix + "/export",
    UPDATE_FLAG = prefix + "/update/flag",
    DELETE_ONE = prefix + "/delete",
    DELETE_ALL = prefix + "/delete/all",
}

/**
 * 获取操作日志
 */
export const getLogOpList = (logOp: logOp) => request.get(API.GET_LIST,
    {
        params: logOp,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 导出操作日志
 */
export const exportLogOp = (logOp: logOp) => request.get(API.EXPORT, {responseType: 'blob', data: logOp});

/**
 * 更新操作日志
 */
export const updateLogOpFlagStatus = (logOp: logOp) => request.post(API.UPDATE_FLAG, logOp);

/**
 * 删除操作日志
 */
export const deleteLogOp = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 删除操作日志
 */
export const deleteLogOpAll = () => request.post(API.DELETE_ALL);

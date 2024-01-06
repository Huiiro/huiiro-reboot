import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/job/log";

export interface sysJobLog {
    logId: number
    jobName: string
    groupName: string
    target: string
    jobStatus: string
    jobMessage: string
    errorInfo: string
    beginTime: string
    endTime: string
    cost: number
}

enum API {
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    DELETE_ONE = prefix + "/delete",
    DELETE_ALL = prefix + "/delete/all",
    EXPORT = prefix + "/export",
}

/**
 * 获取任务日志列表
 */
export const getSysJobLogList = (sysJobLog: sysJobLog) =>
    request.get(API.GET_LIST, {
        params: sysJobLog,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取单个任务日志
 */
export const getSysJobLogSingleton = (sysJobLogId: number | null) => request.get(API.GET_ONE + sysJobLogId);

/**
 * 删除任务日志
 */
export const deleteSysJobLog = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 删除全部任务日志
 */
export const deleteSysJobLogAll = () => request.post(API.DELETE_ALL);

/**
 * 导出任务日志
 */
export const exportSysJobLog = (sysJobLog: sysJobLog | null) => request.get(API.EXPORT, {
    responseType: 'blob',
    data: sysJobLog
});
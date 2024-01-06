import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/job";

export interface sysJob {
    jobId: number
    jobName: string
    groupName: string
    cron: string
    target: string
    jobStatus: string
    concurrentStatus: string
    misfirePolicy: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    UPDATE_STATUS = prefix + "/update/status",
    DELETE_ONE = prefix + "/delete",
    RUN = prefix + "/run"
}

/**
 * 获取任务列表
 */
export const getSysJobList = (sysJob: sysJob) =>
    request.get(API.GET_LIST, {
        params: sysJob,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取单个任务
 */
export const getSysJobSingleton = (sysJobId: number | null) => request.get(API.GET_ONE + sysJobId);

/**
 * 添加任务
 */
export const insertSysJob = (sysJob: sysJob) => request.post(API.INSERT_ONE, sysJob);

/**
 * 更新任务
 */
export const updateSysJob = (sysJob: sysJob) => request.post(API.UPDATE_ONE, sysJob);

/**
 * 更新任务状态
 */
export const updateSysJobStatus = (sysJob: sysJob) => request.post(API.UPDATE_STATUS, sysJob);

/**
 * 删除任务
 */
export const deleteSysJob = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 执行任务
 */
export const runSysJob = (sysJob: sysJob) => request.post(API.RUN, sysJob);

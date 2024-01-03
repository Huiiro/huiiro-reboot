import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/log/login";

export interface logLogin {
    loginId: number
    loginUserName: string
    loginIp: string
    loginAddress: string
    loginTime: string
    loginBrowser: string
    loginOs: string
    loginType: string
    loginStatus: string
    loginMessage: string
}

enum API {
    GET_LIST = prefix + "/list",
    EXPORT = prefix + "/export",
    DELETE_ONE = prefix + "/delete",
    DELETE_ALL = prefix + "/delete/all",
}

/**
 * 获取操作日志
 */
export const getLogLoginList = (logLogin: logLogin) => request.get(API.GET_LIST,
    {
        params: logLogin,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 导出操作日志
 */
export const exportLogLogin = (logLogin: logLogin | null) => request.get(API.EXPORT, {responseType: 'blob', data: logLogin});

/**
 * 删除操作日志
 */
export const deleteLogLogin = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 删除操作日志
 */
export const deleteLogLoginAll = () => request.post(API.DELETE_ALL);

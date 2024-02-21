import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/msg/send/log";

enum API {
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    DELETE_ONE = prefix + "/delete",
    DELETE_ALL = prefix + "/delete/all",
    EXPORT = prefix + "/export",
}

/**
 * 获取消息推送列表
 */
export const getMsgSendLogList = (send: any) =>
    request.get(API.GET_LIST, {
        params: send,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取单个消息推送
 */
export const getMsgSendLogSingleton = (id: number | null) => request.get(API.GET_ONE + id);

/**
 * 删除消息推送
 */
export const deleteMsgSendLog = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 删除全部消息推送
 */
export const deleteMsgSendLogAll = () => request.post(API.DELETE_ALL);

/**
 * 导出消息推送
 */
export const exportMsgSendLog = (send: any | null) => request.get(API.EXPORT, {
    responseType: 'blob',
    data: send
});
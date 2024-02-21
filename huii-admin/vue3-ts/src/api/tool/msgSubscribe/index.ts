import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/msg/sub";

export interface MsgSubscribe {
    subId: number
    subName: string
    subDesc: string
    subStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_LABEL = prefix + "/label",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
    QUERY_NON_SUB_USER = prefix + "/query/non/",
    QUERY_SUB_USER = prefix + "/query/sub/",
    SUB_USER = prefix + "/user/unsub/",
    UNSUB_USER = prefix + "/user/sub/"
}

/**
 * 获取订阅管理列表
 */
export const getMsgSubscribeList = (msgSubscribe: MsgSubscribe) =>
    request.get(API.GET_LIST, {
        params: msgSubscribe,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取邮件模板列表
 */
export const getMsgSubscribeLabel = () => request.get(API.GET_LABEL);

/**
 * 获取单个订阅管理
 */
export const getMsgSubscribeSingleton = (id: number | null) => request.get(API.GET_ONE + id);

/**
 * 添加订阅管理
 */
export const insertMsgSubscribe = (msgSubscribe: MsgSubscribe) => request.post(API.INSERT_ONE, msgSubscribe);

/**
 * 更新订阅管理
 */
export const updateMsgSubscribe = (msgSubscribe: MsgSubscribe) => request.post(API.UPDATE_ONE, msgSubscribe);

/**
 * 删除订阅管理
 */
export const deleteMsgSubscribe = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 查询未订阅用户
 */
export const queryNonSubUser = (param: any, sid: number) => request.get(API.QUERY_NON_SUB_USER + sid, {params: param});

/**
 * 查询已订阅用户
 */
export const querySubUser = (param: any, sid: number) => request.get(API.QUERY_SUB_USER + sid, {params: param});

/**
 * 用户订阅
 */
export const subUser = (userIds: Array<number>, sid: number) => request.post(API.SUB_USER + sid, userIds);

/**
 * 取消用户订阅
 */
export const unsubUser = (userIds: Array<number>, sid: number) => request.post(API.UNSUB_USER + sid, userIds);
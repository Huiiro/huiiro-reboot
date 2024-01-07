import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/msg/send";

export interface msgSendTemplate {
    tempId: number
    tempName: string
    tempParams: string
    sendType: string
    sendTargets: string
    sendTime: string
    sendStatus: string
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
    DELETE_ONE = prefix + "/delete",
    SEND = prefix + "/send"
}

/**
 * 获取发送模板列表
 */
export const getMsgSendTemplateList = (msgSendTemplate: msgSendTemplate) =>
    request.get(API.GET_LIST, {
        params: msgSendTemplate,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取单个发送模板
 */
export const getMsgSendTemplateSingleton = (msgSendTemplateId: number | null) => request.get(API.GET_ONE + msgSendTemplateId);

/**
 * 添加发送模板
 */
export const insertMsgSendTemplate = (msgSendTemplate: msgSendTemplate) => request.post(API.INSERT_ONE, msgSendTemplate);

/**
 * 更新发送模板
 */
export const updateMsgSendTemplate = (msgSendTemplate: msgSendTemplate) => request.post(API.UPDATE_ONE, msgSendTemplate);

/**
 * 删除发送模板
 */
export const deleteMsgSendTemplate = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 发送消息
 */
export const sendMsgTemplate = (msgSendTemplate: msgSendTemplate) => request.post(API.SEND, msgSendTemplate);

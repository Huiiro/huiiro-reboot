import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/msg/mail";

export interface msgMailTemplate {
    mailTempId: number
    mailType: string
    mailSubject: string
    mailContent: string
    mailAttachFile: string
    tempName: string
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
}

/**
 * 获取邮件模板列表
 */
export const getMsgMailTemplateList = (msgMailTemplate: msgMailTemplate) =>
    request.get(API.GET_LIST, {
        params: msgMailTemplate,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });

/**
 * 获取邮件模板列表
 */
export const getMsgMailTemplateLabel = () => request.get(API.GET_LABEL);

/**
 * 获取单个邮件模板
 */
export const getMsgMailTemplateSingleton = (msgMailTemplateId: number | null) => request.get(API.GET_ONE + msgMailTemplateId);

/**
 * 添加邮件模板
 */
export const insertMsgMailTemplate = (msgMailTemplate: msgMailTemplate) => request.post(API.INSERT_ONE, msgMailTemplate);

/**
 * 更新邮件模板
 */
export const updateMsgMailTemplate = (msgMailTemplate: msgMailTemplate) => request.post(API.UPDATE_ONE, msgMailTemplate);

/**
 * 删除邮件模板
 */
export const deleteMsgMailTemplate = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

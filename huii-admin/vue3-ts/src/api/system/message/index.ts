import request from '@/utils/request.ts';

const prefix = "/system/message";

export interface message {
    messageId: number
    sendId: string
    receiveId: string
    message: string
    messageType: string
    messageStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_LIST_MY = prefix + "/list/my",
    GET_UNREAD_COUNT = prefix + "/unread",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取消息列表
 */
export const getMessageList = (message: message) => request.get(API.GET_LIST, {params: message});

/**
 * 获取我的消息列表
 */
export const getMessageListMy = (message: message) => request.get(API.GET_LIST_MY, {params: message});

/**
 * 获取未读消息数量
 */
export const getMessageUnreadCount = () => request.get(API.GET_UNREAD_COUNT);

/**
 * 获取单个消息
 */
export const getMessageSingleton = (messageId: number) => request.get(API.GET_ONE + messageId);

/**
 * 添加消息
 */
export const insertMessage = (message: message) => request.post(API.INSERT_ONE, message);

/**
 * 更新消息
 */
export const updateMessage = (message: message) => request.post(API.UPDATE_ONE, message);

/**
 * 删除消息
 */
export const deleteMessage = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

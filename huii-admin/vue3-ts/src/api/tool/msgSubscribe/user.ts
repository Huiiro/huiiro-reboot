import request from '@/utils/request.ts';

const prefix = "/msg/sub/user";

enum API {
    GET_LIST = prefix + "/list",
    SUBSCRIBE = prefix + "/",
}

/**
 * 用户获取订阅列表
 */
export const getMsgSendTemplateList = (param: any) =>
    request.get(API.GET_LIST, {params: param});

/**
 * 用户订阅/取消订阅
 */
export const subscribe = (id: number | null) => request.get(API.SUBSCRIBE + id);
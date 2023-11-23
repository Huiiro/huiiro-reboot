import request from '@/utils/request.ts';

enum API {
    GET_USER_LIST = "/system/user/list"
}

/**
 * 获取用户列表
 * @param data
 */
export const getUserList = (data: any) => request.post(API.GET_USER_LIST, data);


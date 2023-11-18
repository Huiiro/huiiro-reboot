import request from '@/utils/request.ts';
import {userList} from "@/api/system/user/type.ts";

enum API {
    GET_USER_LIST = "/sys/user/list"
}

/**
 * 获取用户列表
 * @param data
 */
export const getUserList = (data: userList) => request.post(API.GET_USER_LIST, data);


import request from '@/utils/request.ts';
import {AccountLogin} from "@/api/auth/login/type.ts";

enum API {
    ACC_LOGIN = "/auth/v1/login/account"
}

/**
 * 登录接口
 * @param data AccLogin
 */
export const accountLogin = (data: AccountLogin) => request.post(API.ACC_LOGIN, data);
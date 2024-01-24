import request from '@/utils/request.ts';
import {AccountLogin} from "@/api/auth/login/type.ts";

//oauth登录接口
const oauthLogin = '/oauth/login';
//oauth管理接口
const oauthUser = '/oauth/user';

//登录回调地址 1--local 2--back 3--prod
//需在app内同步修改回调地址
const oauthType = '2';

enum API {
    GITHUB_LOGIN = oauthLogin + "/github/" + oauthType,
    GITEE_LOGIN = oauthLogin + "/gitee/" + oauthType,
    OAUTH_BIND_LIST = oauthUser + "/bind/status",
    OAUTH_CANCEL_BIND = oauthUser + "/bind/cancel/",
    OAUTH_BIND_HAS = oauthUser + "/bind/has",
}

/**
 * github 登录
 */
export const githubLogin = () => request.post(API.GITHUB_LOGIN);

/**
 * gitee 登录
 */
export const giteeLogin = () => request.post(API.GITEE_LOGIN);

/**
 * 获取绑定情况
 */
export const getBindList = () => request.get(API.OAUTH_BIND_LIST);

/**
 * 取消绑定
 */
export const cancelBind = (provider: string) => request.post(API.OAUTH_CANCEL_BIND + provider);

/**
 * 绑定第三方账号至已有帐号
 */
export const bindToHas = (data: AccountLogin) => request.post(API.OAUTH_BIND_HAS, data);
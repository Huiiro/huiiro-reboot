import request from '@/utils/request.ts';
import {AccountLogin, EmailLogin, SmsLogin} from "@/api/auth/login/type.ts";

const prefix = '/auth/v1/login';

enum API {
    INFO = prefix + "/info",
    ACC_LOGIN = prefix + "/account",
    SMS_LOGIN = prefix + "/sms",
    EMAIL_LOGIN = prefix + "/email"
}

/**
 * 获取信息
 */
export const getInfo = () => request.get(API.INFO);

/**
 * 登录接口
 * 账号密码登录
 * @param data AccLogin
 */
export const accountLogin = (data: AccountLogin) => request.post(API.ACC_LOGIN, data);

/**
 * 登录接口
 * 手机验证码登录
 * @param data SmsLogin
 */
export const smsLogin = (data: SmsLogin) => request.post(API.SMS_LOGIN, data);

/**
 * 登录接口
 * 邮箱验证码登录
 * @param data EmailLogin
 */
export const emailLogin = (data: EmailLogin) => request.post(API.EMAIL_LOGIN, data);
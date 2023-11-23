import request from '@/utils/request.ts';
import {AccountLogin, EmailLogin, SmsLogin} from "@/api/auth/login/type.ts";

enum API {
    ACC_LOGIN = "/auth/v1/login/account",
    SMS_LOGIN = "/auth/v1/login/sms",
    EMAIL_LOGIN = "/auth/v1/login/email"
}

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
import request from '@/utils/request.ts';

const prefix = "/system/user/profile";

enum API {
    GET_PROFILE = prefix + "/",
    UPDATE_PROFILE = prefix + "/update",
    UPDATE_AVATAR = prefix + "/avatar",
    RESET_PWD = prefix + "/reset/pwd",
    FORGET_PWD_CHECKCODE = prefix + '/forget/pwd/check',
    FORGET_PWD = prefix + "/forget/pwd",
    BIND = prefix + "/bind",
    GET_UNBIND_EMAIL = prefix + "/email/unbind",
    GET_UNBIND_EMAIL_CHECK = prefix + "/email/unbind/check",
    GET_BIND_EMAIL = prefix + "/email/bind",
    GET_BIND_EMAIL_CHECK = prefix + "/email/bind/check",
}

/**
 * 获取用户资料
 */
export const getUserProfile = () => request.get(API.GET_PROFILE);

/**
 * 更新用户资料
 */
export const updateUserProfile = (up: any) => request.post(API.UPDATE_PROFILE, up);

/**
 * 更新用户头像
 */
export const updateUserAvatar = (file: any) => request.post(API.UPDATE_AVATAR, file);

/**
 * 更新密码
 */
export const resetUserPwd = (pwdEntity: any) => request.post(API.RESET_PWD, pwdEntity);

/**
 * 获取忘记密码验证码
 */
export const getForgetUserPwdCheckCode = (type: any, identify: any) => request.get(API.FORGET_PWD_CHECKCODE,
    {params: {'type': type, 'identify': identify}});

/**
 * 忘记密码
 */
export const forgetUserPwd = (pwdEntity: any) => request.post(API.FORGET_PWD, pwdEntity);

/**
 * 获取用户绑定情况
 */
export const getBind = () => request.get(API.BIND);

/**
 * 获取解绑邮箱验证码
 */
export const getUnbindEmailCode = (data: any) => request.post(API.GET_UNBIND_EMAIL, {email: data});

/**
 * 校验绑定邮箱验证码
 */
export const checkUnbindEmailCode = (data: any) => request.post(API.GET_UNBIND_EMAIL_CHECK, data);


/**
 * 获取绑定邮箱验证码
 */
export const getBindEmailCode = (data: any) => request.post(API.GET_BIND_EMAIL, {email: data});

/**
 * 获取绑定邮箱验证码
 */
export const checkBindEmailCode = (data: any) => request.post(API.GET_BIND_EMAIL_CHECK, data);
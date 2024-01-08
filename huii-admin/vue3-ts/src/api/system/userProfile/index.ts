import request from '@/utils/request.ts';

const prefix = "/system/user/profile";

enum API {
    GET_PROFILE = prefix + "/",
    UPDATE_PROFILE = prefix + "/update",
    UPDATE_AVATAR = prefix + "/avatar",
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
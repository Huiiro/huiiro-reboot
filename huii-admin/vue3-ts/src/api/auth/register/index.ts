import request from "@/utils/request.ts";

/**
 * 注册接口
 * @param data
 */
export const register = (data: any) => request.post('/register', data);
/**
 * 校验用户名是否注册
 * @param username
 */
export const checkUsername = (username: any) => request.get('/register/check/username', {params: {username}});

import request from "@/utils/request.ts";

/**
 * 获取找回密码验证码
 * @param type
 * @param identify
 */
export const getForgetPwdCode = (type: string, identify: string) => request.get('/forget/pwd/code',
    {params: {type, identify}});

export const sendForgetPwdRequest = (data: any) => request.post('/forget/pwd/check', data)

import request from '@/utils/request.ts';

enum API {
    GEN_TEXT = "/auth/v1/captcha/gen/text",
    GEN_CALC = "/auth/v1/captcha/gen/calc",
    GEN_SLIDE = "/auth/v1/captcha/gen/slide",
    GEN_CLICK_TEXT = "/auth/v1/captcha/gen/click/text",
    CHECK_SLIDE = "/auth/v1/captcha/check/slide",
    CHECK_CLICK_TEXT = "/auth/v1/captcha/check/click/text",
    GEN_SMS = "/auth/v1/captcha/gen/sms",
    GEN_MAIL = "/auth/v1/captcha/gen/mail",
}

/**
 * 获取文字验证码
 */
export const genTextCaptcha = () => request.get(API.GEN_TEXT);

/**
 * 获取计算验证码
 */
export const genCalcCaptcha = () => request.get(API.GEN_CALC);

/**
 * 获取滑动验证码
 */
export const genSlideCaptcha = () => request.post(API.GEN_SLIDE);

/**
 * 校验滑动验证码
 * @param imageKey
 * @param imageCode
 */
export const checkSlideCaptcha = (imageKey: string, imageCode: number) =>
    request.post(API.CHECK_SLIDE, null, {params: {imageKey, imageCode}});

/**
 * 获取点击文字验证码
 */
export const genClickTextCaptcha = () => request.post(API.GEN_CLICK_TEXT);

/**
 * 校验点击文字验证码
 * @param imageKey
 * @param clickValue
 */
export const checkClickTextCaptcha = (imageKey: string, clickValue: number) =>
    request.post(API.CHECK_CLICK_TEXT, null, {params: {imageKey, clickValue}});

/**
 * 获取手机登录验证码
 */
export const genSmsLoginCaptcha = (phone: string, template: string) =>
    request.get(API.GEN_SMS, {params: {phone, template}});

/**
 * 获取邮箱登录验证码
 */
export const genEmailLoginCaptcha = (email: string, template: string) =>
    request.get(API.GEN_MAIL, {params: {email, template}});


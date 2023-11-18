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


export const genSlideCaptcha = () => request.post(API.GEN_SLIDE);

export const genClickTextCaptcha = () => request.post(API.GEN_CLICK_TEXT);

export const checkSlideCaptcha = (imageKey: string, imageCode: number) =>
    request.post(API.GEN_SLIDE, {imageKey, imageCode});

export const checkClickTextCaptcha = (imageKey: string, clickValue: number) =>
    request.post(API.CHECK_CLICK_TEXT, null, {params: {imageKey, clickValue}});
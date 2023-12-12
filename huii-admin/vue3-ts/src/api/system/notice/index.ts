import request from '@/utils/request.ts';

const prefix = "/system/notice";

export interface notice {
    noticeId: number
    noticeTitle: string
    noticeContent: string
    noticeType: number
    noticeStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    EXPORT = prefix + "/export",
    IMPORT = prefix + "/import",
    IMPORT_TEMPLATE_DOWN = prefix + "/import/template/down",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取消息列表
 */
export const getNoticeList = (notice: notice) => request.get(API.GET_LIST, {params: notice});

/**
 * 获取单个消息
 */
export const getNoticeSingleton = (noticeId: number) => request.get(API.GET_ONE + noticeId);

/**
 * 添加消息
 */
export const insertNotice = (notice: notice) => request.post(API.INSERT_ONE, notice);

/**
 * 更新消息
 */
export const updateNotice = (notice: notice) => request.post(API.UPDATE_ONE, notice);

/**
 * 删除消息
 */
export const deleteNotice = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

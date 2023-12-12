import request from '@/utils/request.ts';

const prefix = "/system/post";

export interface post {
    postId: number
    postName: string
    postKey: string
    postDuty: string
    postSeq: number
    postStatus: string
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
 * 获取岗位列表
 */
export const getPostList = (post: post) => request.get(API.GET_LIST, {params: post});

/**
 * 获取单个岗位
 */
export const getPostSingleton = (postId: number) => request.get(API.GET_ONE + postId);

/**
 * 添加岗位
 */
export const insertPost = (post: post) => request.post(API.INSERT_ONE, post);

/**
 * 更新岗位
 */
export const updatePost = (post: post) => request.post(API.UPDATE_ONE, post);

/**
 * 删除岗位
 */
export const deletePost = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

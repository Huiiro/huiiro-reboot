import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/user";

export interface user {
    userId: number
    deptId: number
    userName: string
    nickName: string
    password: string
    phone: string
    email: string
    sexual: string
    avatar: string
    loginIp: string
    loginTime: string
    deleteFlag: string
    userStatus: string
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
    RESET_PWD = prefix + "/reset/pwd",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取用户列表
 */
export const getUserList = (user: user) =>
    request.get(API.GET_LIST,
        {
            params: user,
            paramsSerializer: function (params) {
                return qs.stringify(params, {arrayFormat: 'comma'})
            }
        });

/**
 * 获取单个用户
 */
export const getUserSingleton = (userId: number | null) => request.get(API.GET_ONE + userId);
export const getUserSingletonInsert = () => request.get(API.GET_ONE);
/**
 * 添加用户
 */
export const insertUser = (user: user) => request.post(API.INSERT_ONE, user);

/**
 * 更新用户
 */
export const updateUser = (user: user) => request.post(API.UPDATE_ONE, user);

/**
 * 重置密码
 */
export const resetPwd = (user: user) => request.post(API.RESET_PWD, user);

/**
 * 删除用户
 */
export const deleteUser = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 导入用户
 */
export const importUser = () => {return API.IMPORT};

/**
 * 获取导入模板
 */
export const getExportUserTemplate = () => request.get(API.IMPORT_TEMPLATE_DOWN, {responseType: 'blob'});

/**
 * 导出用户
 */
export const exportUser = (user: user | null) => request.get(API.EXPORT, {responseType: 'blob', data: user});

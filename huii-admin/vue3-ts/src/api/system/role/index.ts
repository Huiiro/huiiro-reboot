import request from '@/utils/request.ts';

const prefix = "/system/role";

export interface role {
    roleName: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    UPDATE_AUTH = prefix + "/update/auths",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取角色
 */
export const getRoleList = (role: role) =>
    request.get(API.GET_LIST, {params: role});

/**
 * 获取单个角色
 */
export const getRoleSingleton = (roleId: number) => request.get(API.GET_ONE + roleId);

/**
 * 添加角色
 */
export const insertRole = (role: role) => request.post(API.INSERT_ONE, role);

/**
 * 更新角色
 */
export const updateRole = (role: role) => request.post(API.UPDATE_ONE, role);

/**
 * 更新角色
 */
export const updateRoleAuth = (role: role) => request.post(API.UPDATE_AUTH, role);

/**
 * 删除菜单
 */
export const deleteRole = (ids: Array<number>) =>
    request.post(API.DELETE_ONE, ids);
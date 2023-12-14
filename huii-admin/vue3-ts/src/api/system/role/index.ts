import request from '@/utils/request.ts';

const prefix = "/system/role";

export interface role {
    roleId: number
    roleName: string
    roleKey: string
    roleScope: number
    roleSeq: number
    roleStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    EXPORT = prefix + "/export",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    UPDATE_AUTH = prefix + "/update/auths",
    UPDATE_SCOPE = prefix + "/update/scope",
    DELETE_ONE = prefix + "/delete",
    QUERY_NON_AUTH_USER = prefix + "/query/non",
    QUERY_AUTH_USER = prefix + "/query/auth",
    AUTH_USER = prefix + "/user/unauth/",
    UNAUTH_USER = prefix + "/user/auth/"
}

/**
 * 获取角色
 */
export const getRoleList = (role: role) => request.get(API.GET_LIST, {params: role});

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
 * 更新角色菜单权限
 */
export const updateRoleAuth = (role: role) => request.post(API.UPDATE_AUTH, role);

/**
 * 更新角色数据权限
 */
export const updateRoleScope = (role: role) => request.post(API.UPDATE_SCOPE, role);

/**
 * 删除菜单
 */
export const deleteRole = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);


/**
 * 查询未分配用户
 */
export const queryNonAuthUser = (param: any) => request.get(API.QUERY_NON_AUTH_USER, {params: param});

/**
 * 查询已分配用户
 */
export const queryAuthUser = (param: any) => request.get(API.QUERY_AUTH_USER, {params: param});

/**
 * 授权用户
 */
export const authUser = (userIds: Array<number>, roleId: number) => request.post(API.AUTH_USER + roleId, userIds);

/**
 * 取消授权用户
 */
export const unauthUser = (userIds: Array<number>, roleId: number) => request.post(API.UNAUTH_USER + roleId, userIds);

/**
 * 导出角色
 */
export const exportRole = (role: role | null) => request.get(API.EXPORT, {responseType: 'blob', data: role});
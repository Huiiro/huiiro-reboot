import request from '@/utils/request.ts';

const prefix = "/system/dept";

export interface dept {
    deptId: number
    parentId: number
    deptName: string
    deptLeader: string
    deptSeq: number
    deptStatus: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_TREE = prefix + "/tree",
    GET_SELECT = prefix + "/select",
    GET_SELECT_ROLE = prefix + "/select/role/",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete/",
}

/**
 * 获取部门
 */
export const getDeptList = (dept: dept) => request.get(API.GET_LIST, {params: dept});

/**
 * 获取部门树
 */
export const getDeptTree = (dept: dept) => request.get(API.GET_TREE, {params: dept});

/**
 * 获取部门下拉选项
 */
export const getDeptSelect = (dept: dept) => request.get(API.GET_SELECT, {params: dept});

/**
 * 获取部门下拉选项分配数据权限
 */
export const getDeptSelectRole = (roleId: number) => request.get(API.GET_SELECT_ROLE + roleId);

/**
 * 获取单个部门
 */
export const getDeptSingleton = (deptId: number) => request.get(API.GET_ONE + deptId);

/**
 * 添加部门
 */
export const insertDept = (dept: dept) => request.post(API.INSERT_ONE, dept);

/**
 * 更新部门
 */
export const updateDept = (dept: dept) => request.post(API.UPDATE_ONE, dept);

/**
 * 删除部门
 */
export const deleteDept = (allow: string, id: number) => request.post(API.DELETE_ONE + allow + "/" + id);
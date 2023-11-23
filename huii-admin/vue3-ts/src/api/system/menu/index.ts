import request from '@/utils/request.ts';

const prefix = "/system/menu";

export interface menu {
    menuId: number
    parentId: number
    menuType: number
    menuName: string
    menuAuth: string
    menuIcon: string
    menuPath: string
    menuComponent: string
    menuSeq: number
    menuVisible: string
    menuStatus: string
    queryParam: string
    remark: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
}

enum API {
    GET_LIST = prefix + "/list",
    GET_ROUTES = prefix + "/route",
    GET_TREE = prefix + "/tree",
    GET_SELECT = prefix + "/select",
    GET_SELECT_ROLE = prefix + "/select/role/",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    DELETE_ONE = prefix + "/delete/",
}

/**
 * 获取菜单
 */
export const getMenuList = (menu: menu) => request.get(API.GET_LIST, {params: menu});

/**
 * 获取用户路由
 */
export const getRoutes = (menu: menu) => request.get(API.GET_ROUTES, {params: menu});

/**
 * 获取菜单树
 */
export const getMenuTree = (menu: menu) => request.get(API.GET_TREE, {params: menu});

/**
 * 获取菜单下拉选项
 */
export const getMenuSelect = (menu: menu) => request.get(API.GET_SELECT, {params: menu});

/**
 * 获取菜单下拉选项分配角色
 */
export const getMenuSelectRole = (roleId: number) => request.get(API.GET_SELECT_ROLE + roleId);

/**
 * 获取单个菜单
 */
export const getMenuSingleton = (menuId: number) => request.get(API.GET_ONE + menuId);

/**
 * 添加菜单
 */
export const insertMenu = (menu: menu) => request.post(API.INSERT_ONE, menu);

/**
 * 更新菜单
 */
export const updateMenu = (menu: menu) => request.post(API.UPDATE_ONE, menu);

/**
 * 删除菜单
 */
export const deleteMenu = (allow: string, id: number) =>
    request.post(API.DELETE_ONE + allow + "/" + id);
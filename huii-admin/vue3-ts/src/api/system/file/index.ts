import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "/system/file";

export interface sysFile {
    id: string
    fileName: string
    originName: string
    fileSize: string
    fileSuffix: string
    fileAcl: string
    filePrice: string
    accessUrl: string
    fileServer: string
    fileStatus: string
}

enum API {
    UPLOAD = prefix + "/upload",
    GET_LIST = prefix + "/list",
    GET_ONE = prefix + "/",
    INSERT_ONE = prefix + "/insert",
    UPDATE_ONE = prefix + "/update",
    UPDATE_ACL = prefix + "/update/acl",
    UPDATE_STATUS = prefix + "/update/status",
    DELETE_ONE = prefix + "/delete",
}

/**
 * 获取文件列表
 */
export const getSysFileList = (sysFile: sysFile) =>
    request.get(API.GET_LIST, {
        params: sysFile,
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });


/**
 * 更新文件权限
 */
export const updateSysFileAcl = (sysFile: sysFile) => request.post(API.UPDATE_ACL, sysFile);

/**
 * 更新文件状态
 */
export const updateSysFileStatus = (sysFile: sysFile) => request.post(API.UPDATE_STATUS, sysFile);

/**
 * 删除文件
 */
export const deleteSysFile = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);

/**
 * 上传文件
 */
export const uploadSysFile = () => {return API.UPLOAD};
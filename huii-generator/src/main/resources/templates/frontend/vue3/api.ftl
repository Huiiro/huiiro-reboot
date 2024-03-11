import request from '@/utils/request.ts';
//@ts-ignore
import qs from 'qs';

const prefix = "${requestUrl}";

export interface ${variableName} {
<#list columns as col>
    <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">
    ${col.javaField}: <#if col.javaType == "Long" || col.javaType == "Integer" || col.javaType == "Double">number<#else>string</#if>
    </#if>
</#list>
}

enum API {
    <#if genExportInterface == "1">
    EXPORT = prefix + "/export",
    </#if>
    <#if genImportInterface == "1">
    IMPORT = prefix + "/import",
    IMPORT_TEMPLATE_DOWN = prefix + "/import/template/down",
    </#if>
    GET_LIST = prefix + "/list",
    <#if tableTemplate == "2">
    GET_TREE = prefix + "/tree",
    GET_SELECT = prefix + "/select",
    </#if>
    GET_ONE = prefix + "/",
    <#if genAddInterface == "1">
    INSERT_ONE = prefix + "/insert",
    </#if>
    <#if genEditInterface == "1">
    UPDATE_ONE = prefix + "/update",
    </#if>
    <#if genDeleteInterface == "1">
    <#if tableTemplate == "2">
    DELETE_ONE = prefix + "/delete/",
    <#else>
    DELETE_ONE = prefix + "/delete",
    </#if>
    </#if>
}

/**
 * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>列表
 */
export const get${className}List = (${variableName}: ${variableName}) =>
    request.get(API.GET_LIST, {
        params: ${variableName},
        paramsSerializer: function (params) {
            return qs.stringify(params, {arrayFormat: 'comma'})
        }
    });
<#if tableTemplate == "2">

/**
 * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>树表
 */
export const get${className}Tree = (${variableName}: ${variableName}) => request.get(API.GET_TREE, {params: ${variableName}});

/**
 * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>下拉项
 */
export const get${className}Select = (${variableName}: ${variableName}) => request.get(API.GET_SELECT, {params: ${variableName}});
</#if>

/**
 * 获取单个<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const get${className}Singleton = (${variableName}Id: number | null) => request.get(API.GET_ONE + ${variableName}Id);
<#if genAddInterface == "1">

/**
 * 添加<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const insert${className} = (${variableName}: ${variableName}) => request.post(API.INSERT_ONE, ${variableName});
</#if>
<#if genEditInterface == "1">

/**
 * 更新<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const update${className} = (${variableName}: ${variableName}) => request.post(API.UPDATE_ONE, ${variableName});
</#if>
<#if genDeleteInterface == "1">

<#if tableTemplate == "2">
/**
 * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const delete${className} = (allow: string, id: number) => request.post(API.DELETE_ONE + allow + "/" + id);
<#else>
/**
 * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const delete${className} = (ids: Array<number>) => request.post(API.DELETE_ONE, ids);
</#if>
</#if>
<#if genImportInterface == "1">

/**
 * 导入<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const import${className} = () => {return API.IMPORT};

/**
 * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>导入模板
 */
export const getExport${className}Template = () => request.get(API.IMPORT_TEMPLATE_DOWN, {responseType: 'blob'});
</#if>
<#if genExportInterface == "1">

/**
 * 导出<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
 */
export const export${className} = (${variableName}: ${variableName} | null) => request.get(API.EXPORT, {responseType: 'blob', data: ${variableName}});
</#if>
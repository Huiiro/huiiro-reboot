package ${packageName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
<#if tableTemplate != "2">
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
</#if>
import ${packageName}.domain.${className};
<#if tableTemplate == "2">
import com.huii.common.core.model.Tree;
</#if>
<#if tableTemplate == "2" || genExportInterface == "1">

import java.util.List;
</#if>

/**
 * <#if moduleFunctionDesc?has_content>${moduleFunctionDesc}</#if>服务层接口
 *
 * @author ${authorName}
 * @date ${createTime}
 */
public interface ${className}Service extends IService<${className}> {
    <#--普通查询接口-->
    <#if tableTemplate != "2">

    /**
     * 查询<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>分页
     *
     * @param ${variableName} ${variableName}
     * @param pageParam pageParam
     * @return page
     */
    Page select${className}List(${className} ${variableName}, PageParam pageParam);
    </#if>
    <#--查询导出数据接口-->
    <#if tableTemplate == "2" || genExportInterface == "1">

    /**
     * 查询<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>列表
     *
     * @param ${variableName} ${variableName}
     * @return list
     */
    List<${className}> select${className}List(${className} ${variableName});
    </#if>
    <#--查询单条数据接口-->

    /**
     * 查询<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     *
     * @param id id
     * @return ${variableName}
     */
    ${className} select${className}ById(Long id);
    <#--构造树形结果接口-->
    <#if tableTemplate == "2">

    /**
     * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>树表
     *
     * @param list list
     * @return treeList
     */
    List<${className}> buildTree(List<${className}> list);

    <#--构造选项结果接口-->
    /**
     * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>下拉框选项
     *
     * @param list        list
     * @param addHeadNode addHeadNode
     * @return treeListSelect
     */
    List<Tree> buildSelect(List<${className}> list, Boolean addHeadNode);
    </#if>
    <#--新增接口-->

    <#if genAddInterface == "1">
    /**
     * 校验添加<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>数据
     *
     * @param ${variableName} ${variableName}
     */
    void checkInsert(${className} ${variableName});

    /**
     * 添加<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     *
     * @param ${variableName} ${variableName}
     */
    void insert${className}(${className} ${variableName});
    </#if>
    <#--修改接口-->
    <#if genEditInterface == "1">

    /**
     * 校验修改<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>数据
     *
     * @param ${variableName} ${variableName}
     */
    void checkUpdate(${className} ${variableName});

    /**
     * 修改<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     *
     * @param ${variableName} ${variableName}
     */
    void update${className}(${className} ${variableName});
    </#if>
    <#--删除接口-->
    <#if genDeleteInterface == "1">

    <#if tableTemplate == "2">
    /**
     * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     *
     * @param allow allow
     * @param id    id
     */
    void delete${className}(String allow, Long id);
    <#else>
    /**
     * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     *
     * @param ids ids
     */
    void delete${className}(Long[] ids);
    </#if>
    </#if>
}
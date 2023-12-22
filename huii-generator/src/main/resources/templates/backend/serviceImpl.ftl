package ${packageName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
<#if tableTemplate != "2">
import com.baomidou.mybatisplus.core.metadata.IPage;
</#if>
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
<#if tableTemplate == "2">
import com.huii.common.constants.SystemConstants;
</#if>
<#if tableTemplate != "2">
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
</#if>
<#assign uniqueCheckExecuted = false>
<#list columns as col>
<#if col.checkUnique == "1" && !uniqueCheckExecuted>
import com.huii.common.exception.ServiceException;
<#assign uniqueCheckExecuted = true>
</#if>
</#list>
<#if tableTemplate == "2">
import com.huii.common.core.model.Tree;
</#if>
<#if tableTemplate != "2">
import com.huii.common.utils.PageParamUtils;
</#if>
import com.huii.common.utils.TimeUtils;
import ${packageName}.domain.${className};
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.service.${className}Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
<#assign uniqueCheckExecuted = false>
<#list columns as col>
<#if col.checkUnique == "1" && !uniqueCheckExecuted>
import org.apache.commons.lang3.StringUtils;
<#assign uniqueCheckExecuted = true>
</#if>
</#list>
import org.springframework.stereotype.Service;

<#if tableTemplate == "2">
import java.util.ArrayList;
</#if>
<#if tableTemplate != "2">
import java.util.Arrays;
</#if>
<#if tableTemplate == "2" || genExportInterface == "1">
import java.util.List;
</#if>
import java.util.Map;

/**
 * <#if moduleFunctionDesc?has_content>${moduleFunctionDesc}</#if>服务层实现
 *
 * @author ${authorName}
 * @date ${createTime}
 */
@Service
@RequiredArgsConstructor
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

    private final ${className}Mapper ${variableName}Mapper;

    <#if tableTemplate != "2">
    @Override
    public Page select${className}List(${className} ${variableName}, PageParam pageParam) {
        IPage<${className}> iPage = new PageParamUtils<${className}>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(${variableName})));
    }
    </#if>
    <#if tableTemplate == "2" || genExportInterface == "1">
    @Override
    public List<${className}> select${className}List(${className} ${variableName}) {
        return ${variableName}Mapper.selectList(wrapperBuilder(${variableName}));
    }
    </#if>

    @Override
    public ${className} select${className}ById(Long id) {
        return ${variableName}Mapper.selectById(id);
    }
    <#if tableTemplate == "2">

    @Override
    public List<${className}> buildTree(List<${className}> list) {
        return treeBuilder(0L, list);
    }

    @Override
    public List<Tree> buildSelect(List<${className}> list, Boolean addHeadNode) {
        List<Tree> children = Tree.mapToTree(list, ${className}::get${treeId?cap_first}, ${className}::get${treeLabelName?cap_first}, ${className}::getChildren);
        if (addHeadNode) {
            Tree tree = new Tree(0L, "顶级${moduleFunctionDesc}", children);
            List<Tree> header = new ArrayList<>(1);
            header.add(tree);
            return header;
        }
        return children;
    }
    </#if>
    <#if genAddInterface == "1">

    @Override
    public void checkInsert(${className} ${variableName}) {
        <#list columns as col>
        <#if col.checkUnique == "1">
        if (${variableName}Mapper.exists(new LambdaQueryWrapper<${className}>()
                .eq(${className}::get${col.javaField?cap_first}, ${variableName}.get${col.javaField?cap_first}()))) {
            throw new ServiceException("<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>重复");
        }
        </#if>
        </#list>
    }

    @Override
    public void insert${className}(${className} ${variableName}) {
        ${variableName}Mapper.insert(${variableName});
    }
    </#if>
    <#if genEditInterface == "1">

    @Override
    public void checkUpdate(${className} ${variableName}) {
        <#assign needQueryOldOne = false>
        <#list columns as col>
        <#if col.checkUnique == "1">
        <#assign needQueryOldOne = true>
        </#if>
        </#list>
        <#if needQueryOldOne>
        ${className} oldOne = ${variableName}Mapper.selectById(<#list columns as col><#if col.isPrimaryKey == "1">${variableName}.get${col.javaField?cap_first}()</#if></#list>);
        </#if>
        <#list columns as col>
        <#if col.checkUnique == "1">
        if (!StringUtils.equals(${variableName}.get${col.javaField?cap_first}(), oldOne.get${col.javaField?cap_first}())) {
            if (${variableName}Mapper.exists(new LambdaQueryWrapper<${className}>()
                    .eq(${className}::get${col.javaField?cap_first}, ${variableName}.get${col.javaField?cap_first}()))) {
                throw new ServiceException("<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>重复");
            }
        }
        </#if>
        </#list>
    }

    @Override
    public void update${className}(${className} ${variableName}) {
        ${variableName}Mapper.updateById(${variableName});
    }
    </#if>
    <#if genDeleteInterface == "1">

    <#if tableTemplate == "2">
    @Override
    public void delete${className}(String allow, Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (SystemConstants.STATUS_1.equals(allow)) {
            List<${className}> list = ${variableName}Mapper.selectList(null);
            selectChildrenIds(id, list, ids);
            ${variableName}Mapper.deleteBatchIds(ids);
        } else {
            boolean existsChildren = ${variableName}Mapper.exists(new LambdaQueryWrapper<${className}>()
                    .eq(${className}::getParentId, id));
            if (existsChildren) {
                throw new ServiceException("存在子${moduleFunctionDesc}，不能删除");
            }
            ${variableName}Mapper.deleteById(id);
        }
    }
    <#else>
    @Override
    public void delete${className}(Long[] ids) {
        ${variableName}Mapper.deleteBatchIds(Arrays.asList(ids));
    }
    </#if>
    </#if>

    private LambdaQueryWrapper<${className}> wrapperBuilder(${className} ${variableName}) {
        Map<String, Object> params = ${variableName}.getParams();
        LambdaQueryWrapper<${className}> wrapper = new LambdaQueryWrapper<>();
        <#assign isFirst = false>
        <#list columns as col>
        <#if col.queryType?has_content>
        <#if !isFirst>
        wrapper.${col.queryType}(ObjectUtils.isNotEmpty(${variableName}.get${col.javaField?cap_first}()), ${className}::get${col.javaField?cap_first}, ${variableName}.get${col.javaField?cap_first}())
            <#assign isFirst = true>
        <#else>
                .${col.queryType}(ObjectUtils.isNotEmpty(${variableName}.get${col.javaField?cap_first}()), ${className}::get${col.javaField?cap_first}, ${variableName}.get${col.javaField?cap_first}())
        </#if>
        </#if>
        </#list>
                .between(ObjectUtils.isNotEmpty(params.get("beginTime")) &&
                                ObjectUtils.isNotEmpty(params.get("endTime")),
                        ${className}::getCreateTime, TimeUtils.stringToLocalDateTime((String) params.get("beginTime")),
                        TimeUtils.stringToLocalDateTime((String) params.get("endTime")))
                .orderByAsc(${className}::getCreateTime);
        return wrapper;
    }
    <#if tableTemplate == "2">

    private List<${className}> treeBuilder(Long pid, List<${className}> list) {
        List<${className}> children = new ArrayList<>();
        for (${className} item : list) {
            if (pid.equals(item.getParentId())) {
                item.setChildrenFlag(false);
                children.add(item);
            }
        }
        for (${className} t : children) {
            List<${className}> tList = treeBuilder(t.get${treeId?cap_first}(), list);
            if (!tList.isEmpty()) {
                t.setChildrenFlag(true);
            }
            t.setChildren(tList);
        }
        return children;
    }

    private void selectChildrenIds(Long pid, List<${className}> list, List<Long> ids) {
        for (${className} ele : list) {
            if (pid.equals(ele.getParentId())) {
                ids.add(ele.get${treeId?cap_first}());
                selectChildrenIds(ele.get${treeId?cap_first}(), list, ids);
            }
        }
    }
    </#if>
}
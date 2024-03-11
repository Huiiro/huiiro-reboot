package ${packageName}.controller;

import com.huii.common.annotation.Log;
<#if genExportInterface == "1">
import com.huii.common.annotation.RepeatSubmit;
</#if>
<#if tableTemplate != "2">
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
</#if>
import com.huii.common.core.model.R;
<#if tableTemplate == "2">
import com.huii.common.core.model.Tree;
</#if>
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
<#if genImportInterface == "1">
import com.huii.common.excel.ExcelResult;
</#if>
<#if genExportInterface == "1">
import com.huii.common.utils.BeanCopyUtils;
</#if>
<#if genExportInterface == "1" || genImportInterface == "1">
import com.huii.common.utils.ExcelUtils;
</#if>
import ${packageName}.domain.${className};
<#if genExportInterface == "1">
import ${packageName}.domain.vo.${className}ExcelExportVo;
</#if>
<#if genImportInterface == "1">
import ${packageName}.domain.vo.${className}ExcelImportVo;
</#if>
import ${packageName}.service.${className}Service;
<#if genExportInterface == "1">
import jakarta.servlet.http.HttpServletResponse;
</#if>
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
<#if genImportInterface == "1">
import org.springframework.web.multipart.MultipartFile;
</#if>
<#if genImportInterface == "1" || tableTemplate == "2" || genExportInterface == "1">

</#if>
<#if genImportInterface == "1">
import java.io.IOException;
</#if>
<#if genImportInterface == "1">
import java.util.ArrayList;
</#if>
<#if tableTemplate == "2" || genExportInterface == "1">
import java.util.List;
</#if>

/**
 * <#if moduleFunctionDesc?has_content>${moduleFunctionDesc}</#if>服务层实现
 *
 * @author ${authorName}
 * @date ${createTime}
 */
@Validated
@RestController
@RequestMapping("${requestUrl}")
@RequiredArgsConstructor
public class ${className}Controller extends BaseController {

    private final ${className}Service ${variableName}Service;
    <#if genExportInterface == "1">

    <#--导出接口-->
    /**
     * 导出<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.EXPORT)
    public void export${className}(${className} ${variableName}, HttpServletResponse response) {
        List<${className}> list = ${variableName}Service.select${className}List(${variableName});
        List<${className}ExcelExportVo> vos = BeanCopyUtils.copyList(list, ${className}ExcelExportVo.class);
        ExcelUtils.exportExcel(null, vos, ${className}ExcelExportVo.class, response);
    }
    </#if>
    <#if genImportInterface == "1">

    /**
     * 导出<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>模板
     */
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/import/template/down")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtils.exportExcel("<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>导入模板", new ArrayList<>(), ${className}ExcelImportVo.class, response);
    }

    <#--导入接口-->
    /**
     * 导入<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:import')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/import{update}")
    @Log(value = "导入<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.IMPORT)
    public R<ExcelResult<${className}ExcelImportVo>> import${className}(@RequestPart("file") MultipartFile file,
                                @PathVariable(required = false) Boolean update) throws IOException {
        ExcelResult<${className}ExcelImportVo> result = ExcelUtils.importAsyncExcel(
                file.getInputStream(), ${className}ExcelImportVo.class, null);//导入操作请自行实现监听器
        return R.ok(result);
    }
    </#if>
    <#if tableTemplate != "2">

    <#--查询接口-->
    /**
     * 查询<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>分页
     */
    @GetMapping("/list")
    public R<Page> getList(${className} ${variableName}, PageParam pageParam) {
        Page page = ${variableName}Service.select${className}List(${variableName}, pageParam);
        return R.ok(page);
    }
    </#if>
    <#--树表接口-->
    <#if tableTemplate == "2">

    /**
     * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>列表
     */
    @GetMapping("/list")
    public R<List<${className}>> getList(${className} ${variableName}) {
        List<${className}> list = ${variableName}Service.select${className}List(${variableName});
        return R.ok(list);
    }

    /**
     * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>树表
     */
    @GetMapping("/tree")
    public R<List<${className}>> getTreeList(${className} ${variableName}) {
        List<${className}> select = ${variableName}Service.select${className}List(${variableName});
        List<${className}> list = ${variableName}Service.buildTree(select);
        return R.ok(list);
    }

    /**
     * 获取<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>下拉项
     */
    @GetMapping("/select")
    public R<List<Tree>> getTreeSelect(${className} ${variableName}) {
        List<${className}> select = ${variableName}Service.select${className}List(${variableName});
        List<${className}> tree = ${variableName}Service.buildTree(select);
        List<Tree> list = ${variableName}Service.buildSelect(tree, true);
        return R.ok(list);
    }
    </#if>

    <#--查询单条接口-->
    /**
     * 查询<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @GetMapping(value = "/{id}")
    public R<${className}> get${className}(@PathVariable Long id) {
        return R.ok(${variableName}Service.select${className}ById(id));
    }
    <#--新增接口-->
    <#if genAddInterface == "1">

    /**
     * 新增<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:add')")
    @PostMapping("/insert")
    @Log(value = "新增<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.INSERT)
    public R<Void> insert${className}(@Validated @RequestBody ${className} ${variableName}) {
        ${variableName}Service.insert${className}(${variableName});
        return saveSuccess();
    }
    </#if>
    <#--更新接口-->
    <#if genEditInterface == "1">

    /**
     * 更新<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:edit')")
    @PostMapping("/update")
    @Log(value = "更新<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> update${className}(@Validated @RequestBody ${className} ${variableName}) {
        ${variableName}Service.update${className}(${variableName});
        return updateSuccess();
    }
    </#if>
    <#--删除接口-->
    <#if genDeleteInterface == "1">

    <#if tableTemplate == "2">
    /**
     * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:delete')")
    @PostMapping("/delete/{allow}/{id}")
    @Log(value = "删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> delete${className}(@PathVariable String allow, @PathVariable Long id) {
        ${variableName}Service.delete${className}(allow, id);
        return deleteSuccess();
    }
    <#else>
    /**
     * 删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>
     */
    @PreAuthorize("@ap.hasAuth('${authPrefix}:delete')")
    @PostMapping("/delete")
    @Log(value = "删除<#if moduleFunctionName?has_content>${moduleFunctionName}</#if>", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> delete${className}(@RequestBody Long[] ids) {
        ${variableName}Service.delete${className}(ids);
        return deleteSuccess();
    }
    </#if>
    </#if>
}
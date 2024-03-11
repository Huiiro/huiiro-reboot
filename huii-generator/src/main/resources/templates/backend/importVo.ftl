package ${packageName}.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
<#assign importedExcelAnnotation = false>
<#list columns as col>
<#if col.dicType?has_content && !importedExcelAnnotation>
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
<#assign importedExcelAnnotation = true>
</#if>
</#list>
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ${className}ExcelImportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    <#list columns as col>
    <#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">

    <#if col.dicType?has_content>
    @ExcelProperty(value = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>", converter = ExcelDataConvert.class)
    @ExcelData(dictType = "${col.dicType}")
    <#else>
    @ExcelProperty(value = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>")
    </#if>
    private ${col.javaType} ${col.javaField};
    </#if>
    </#list>
}
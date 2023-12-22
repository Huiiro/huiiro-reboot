package ${packageName}.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
<#list columns as col>
<#if col.dicType?has_content>
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
</#if>
</#list>
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ${className}ExcelExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    <#list columns as col>

    <#if col.dicType?has_content>
    @ExcelProperty(value = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>", converter = ExcelDataConvert.class)
    @ExcelData(dictType = "${col.dicType}")
    <#else>
    @ExcelProperty(value = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>")
    </#if>
    private ${col.javaType} ${col.javaField};
    </#list>
}
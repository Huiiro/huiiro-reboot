package ${packageName}.domain;

<#--导入mbp注解-->
import com.baomidou.mybatisplus.annotation.*;
<#--导入基类实体-->
<#if tableTemplate == "2">
import com.huii.common.core.model.base.TreeEntity;
<#else>
import com.huii.common.core.model.base.BaseEntity;
</#if>
<#--导入非空注解-->
<#assign importedNotBlank = false>
<#assign importedNotNull = false>
<#list columns as col>
<#if col.isRequired == "1">
<#if col.javaType == "String" && !importedNotBlank>
import jakarta.validation.constraints.NotBlank;
<#assign importedNotBlank = true>
<#elseif col.javaType != "String" && !importedNotNull>
import jakarta.validation.constraints.NotNull;
<#assign importedNotNull = true>
</#if>
</#if>
</#list>
<#--导入lombok注解-->
import lombok.Data;
import lombok.EqualsAndHashCode;

<#--导入java包-->
<#assign importedBigDecimal = false>
<#assign importedLocalDateTime = false>
<#assign importedDate = false>
<#list columns as col>
<#if col.javaType == "BigDecimal" && !importedBigDecimal>
import java.math.BigDecimal;
<#assign importedBigDecimal = true>
<#elseif col.javaType == "LocalDateTime" && !importedLocalDateTime>
import java.time.LocalDateTime;
<#assign importedLocalDateTime = true && !importedDate>
<#elseif col.javaType == "Date">
import java.util.Date;
<#assign importedDate = true>
</#if>
</#list>

/**
 * <#if moduleFunctionDesc?has_content>${moduleFunctionDesc}</#if>实体类
 *
 * @author ${authorName}
 * @date ${createTime}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
<#--主键相关注解-->
<#--postgres-->
<#if sqlType == "2">
@KeySequence(value = "${tableName}_id_seq", dbType = DbType.POSTGRE_SQL)
</#if>
<#--实体类继承信息-->
<#if tableTemplate == "2">
public class ${className} extends TreeEntity<${className}> {
<#else>
public class ${className} extends BaseEntity {
</#if>
<#list columns as col>
<#if col.javaField !="updateBy" && col.javaField !="createBy" && col.javaField != "createTime" && col.javaField != "updateTime">

    /**
     * <#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>
     */
    <#--主键校验注解-->
    <#if col.isPrimaryKey == "1">
    <#if sqlType == "1"><#--mysql-->
    @TableId(value = "${col.columnName}"<#if col.isIncrement == "1">, type = IdType.AUTO</#if>)
    <#elseif sqlType == "2"><#--postgres-->
    @TableId(value = "${col.columnName}", type = IdType.INPUT)
    </#if>
    </#if>
    <#--非空校验注解-->
    <#if col.isRequired == "1">
    <#if col.javaType == "String">
    @NotBlank(message = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>不为空")
    <#else>
    @NotNull(message = "<#if col.columnComment?has_content>${col.columnComment}<#else>${col.javaField}</#if>不为空")
    </#if>
    </#if>
    private ${col.javaType} ${col.javaField};
</#if>
</#list>
}

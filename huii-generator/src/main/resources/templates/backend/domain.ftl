package ${packageName}.domain;

import com.baomidou.mybatisplus.annotation.*;
<#if tableTemplate == "2">
import com.huii.common.core.model.base.TreeEntity;
<#else>
import com.huii.common.core.model.base.BaseEntity;
</#if>
<#list columns as col>
<#if col.isRequired == "1">
<#if col.javaType == "String">
import jakarta.validation.constraints.NotBlank;
<#else>
import jakarta.validation.constraints.NotNull;
</#if>
</#if>
</#list>
import lombok.Data;
import lombok.EqualsAndHashCode;

<#list columns as col>
<#if col.javaType == "BigDecimal">
import java.math.BigDecimal;
<#elseif col.javaType == "LocalDateTime">
import java.util.LocalDateTime;
<#elseif col.javaType == "Date">
import java.util.Date;
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
<#if sqlType == "2"><#--postgres-->
@KeySequence(value = "${tableName}_id_seq", dbType = DbType.POSTGRE_SQL)
</#if>
<#--是否是树表-->
<#if tableTemplate == "2">
public class ${className} extends TreeEntity<${className}> {
<#else>
public class ${className} extends BaseEntity {
</#if>
<#list columns as col>

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
</#list>
}

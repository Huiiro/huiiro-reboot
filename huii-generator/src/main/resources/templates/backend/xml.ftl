<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.mapper.${className}Mapper">

    <resultMap type="${packageName}.domain.${className}" id="${className}Result">
        <#list columns as col>
        <#if col.isPrimaryKey == "1">
        <id property="${col.javaField}" column="${col.columnName}"/>
        <#else>
        <result property="${col.javaField}" column="${col.columnName}"/>
        </#if>
        </#list>
    </resultMap>

    <sql id="select${className}">
        <#list columns as col>
        <#if col_index == 0>
        select a.${col.columnName}<#if col_has_next>,</#if>
        <#else>
               a.${col.columnName}<#if col_has_next>,</#if>
        </#if>
        </#list>
        from ${tableName} a
    </sql>

</mapper>

package com.huii.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("gen_column")
@KeySequence(value = "gen_column_id_seq", dbType = DbType.POSTGRE_SQL)
public class GenColumn extends BaseEntity {

    /**
     * 列ID
     */
    @TableId(value = "column_id", type = IdType.INPUT)
    private Long columnId;

    /**
     * 表ID
     */
    private Long tableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述（注释）
     */
    private String columnComment;

    /**
     * 数据库字段类型
     */
    private String sqlType;

    /**
     * 数据库字段名称
     */
    private String sqlField;

    /**
     * 数据库字段长度
     */
    private String sqlCharLength;

    /**
     * 数据库字段排序
     */
    private String sqlOrder;

    /**
     * java字段类型
     */
    private String javaType;

    /**
     * java字段名
     */
    private String javaField;

    /**
     * 是否是主键
     */
    private String isPrimaryKey;

    /**
     * 是否自增
     */
    private String isIncrement;

    /**
     * 是否为必填字段
     * 设置notBlank notNull等注解
     */
    private String isRequired;

    /**
     * 是否校验字段长度大小
     * 格式： 1/0,min,max,message
     */
    @Deprecated
    private String checkSize;

    /**
     * 是否是不可重复字段
     * 将在插入和修改时进行校验 将抛出异常信息
     */
    private String checkUnique;

    /**
     * wrapper查询方式
     * EQ, NE, GE, LE, GT, LT, BETWEEN, LIKE...
     */
    private String queryType;

    /**
     * 前端是否查询此字段， 将会生成 query表单查询和 header表单查询
     */
    private String isQueryField;

    /**
     * 前端表单数据展示类型
     * input
     * textarea...
     * datetime
     */
    private String formType;

    /**
     * 字典类型
     */
    private String dicType;

    private String remark;
}

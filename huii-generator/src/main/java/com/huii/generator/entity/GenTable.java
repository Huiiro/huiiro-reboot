package com.huii.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table")
@KeySequence(value = "gen_table_id_seq", dbType = DbType.POSTGRE_SQL)
public class GenTable extends BaseEntity {

    /**
     * 表ID
     */
    @TableId(value = "table_id", type = IdType.INPUT)
    private Long tableId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 表模板
     * 1--单表， 2--树表， 3--关联表
     */
    private String tableTemplate;

    /**
     * 实体类名称
     */
    private String className;

    /**
     * 变量名
     */
    private String variableName;

    /**
     * 前端版本 vue2版本和vue3版本
     * 目前只支持vue3
     * 后续可能会完善vue2版本
     * 1--vue2, 2--vue3
     */
    private String frontendType;

    /**
     * 数据库类型
     * 目前只支持pg
     * 后续可能考虑加入对其他数据库支持
     * 1-- mysql, 2-- postgresql.ftl, 3--oracle, 4-- sqlserver
     */
    private String sqlType;

    /**
     * 作者名称
     * 默认huii
     */
    private String authorName;

    /**
     * 生成包路径
     * 包的全路径，将在该目录下按结构生成对应文件
     * 默认根路径
     */
    private String packageName;

    /**
     * 生成模块名称
     * 系统将按照模块名称分包，默认只有一级包路径
     */
    private String moduleName;

    /**
     * 模块功能描述
     * 描述模块作用，将用于生成注释
     */
    private String moduleFunctionDesc;

    /**
     * 模块功能名称
     * 描述模块作用 例如用户
     */
    private String moduleFunctionName;

    /**
     * 权限字段前缀
     * 例如用户权限校验的前缀: system:user:xx
     * 此字段为空时，默认不会校验菜单权限
     * xx: add, edit, delete, query, insert, upload
     */
    private String authPrefix;

    /**
     * 请求路径字段
     * 控制器请求路段前缀，以'/'开头，例如 /system/user
     */
    private String requestUrl;

    /**
     * 生成的接口类型
     * 生成添加接口
     */
    private String genAddInterface;

    /**
     * 生成的接口类型
     * 生成修改接口
     */
    private String genEditInterface;

    /**
     * 生成的接口类型
     * 生成删除接口
     */
    private String genDeleteInterface;

    /**
     * 生成的接口类型
     * 生成上传接口
     */
    private String genImportInterface;

    /**
     * 生成的接口类型
     * 生成下载接口
     */
    private String genExportInterface;

    /**
     * 关联表名称
     */
    private String subTableName;

    /**
     * 关联外键字段
     */
    private String subTableForeignKey;

    /**
     * 树表 ID 字段
     */
    private String treeId;

    /**
     * 树表 Label 字段
     */
    private String treeLabelName;

    /**
     * 上级菜单ID
     * 用于生成 menu 对应的sql
     */
    private Long parentMenuId;

    private String remark;

    @TableField(exist = false)
    private List<GenColumn> columns;

}
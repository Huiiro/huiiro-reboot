package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典类型 实体类
 * sys_dic_type
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dic_type")
@KeySequence(value = "sys_dic_type_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysDicType extends BaseEntity {

    @TableId(value = "type_id", type = IdType.INPUT)
    @ExcelColumn(value = "字典类型ID")
    private Long typeId;

    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 50, message = "字典类型名称长度不能超过{max}个字符")
    @ExcelColumn(value = "字典名称")
    private String typeName;

    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 50, message = "字典类型类型长度不能超过{max}个字符")
    @ExcelColumn(value = "字典类型")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String dicType;

    @ExcelColumn(value = "字典类型状态", convert = "0=禁用,1=正常")
    private String typeStatus;

    @ExcelColumn(value = "字典类型备注")
    private String remark;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    @ExcelColumn(export = false)
    private List<SysDicData> data = new ArrayList<>();
}
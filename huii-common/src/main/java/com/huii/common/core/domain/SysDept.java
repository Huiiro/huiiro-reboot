package com.huii.common.core.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
import com.huii.common.core.model.base.TreeEntity;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 部门 实体类
 * sys_dept
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@KeySequence(value = "sys_dept_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysDept extends TreeEntity<SysDept> {

    @TableId(value = "dept_id", type = IdType.INPUT)
    @ExcelProperty(value = "部门ID")
    private Long deptId;

    @NotBlank(message = "部门名称不为空")
    @Size(min = 0, max = 50, message = "部门名称长度不能超过{max}个字符")
    @ExcelProperty("部门名称")
    private String deptName;

    @Size(min = 0, max = 255, message = "部门负责人信息长度不能超过{max}个字符")
    @ExcelProperty(value = "部门负责人信息")
    private String deptLeader;

    @NotNull(message = "部门顺序不为空")
    @Min(value = 0, message = "部门顺序应大于等于 {value}")
    @Max(value = 999, message = "部门顺序应小于等于 {value}")
    @ExcelProperty("部门顺序")
    private Integer deptSeq;

    @ExcelProperty(value = "部门状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String deptStatus;

    @ExcelProperty(value = "部门备注")
    private String remark;

}
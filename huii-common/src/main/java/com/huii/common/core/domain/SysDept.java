package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.TreeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class SysDept extends TreeEntity<SysDept> {

    @TableId(value = "dept_id", type = IdType.AUTO)
    @ExcelColumn(value = "部门ID")
    private Long deptId;

    @NotBlank(message = "部门名称不为空")
    @Size(min = 0, max = 50, message = "部门名称长度不能超过{max}个字符")
    @ExcelColumn("部门名称")
    private String deptName;

    @Size(min = 0, max = 255, message = "部门负责人信息长度不能超过{max}个字符")
    @ExcelColumn(value = "部门负责人信息")
    private String deptLeader;

    @NotNull(message = "部门顺序不为空")
    @Size(min = 0, max = 999, message = "部门顺序应在{min}-{max}之间")
    @ExcelColumn("部门顺序")
    private Integer deptSeq;

    @ExcelColumn(value = "部门状态", convert = "0=禁用,1=正常")
    private Integer deptStatus;

    @ExcelColumn(value = "部门备注")
    private String remark;
}
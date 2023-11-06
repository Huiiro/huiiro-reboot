package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色 实体类
 * sys_role
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("public.sys_role")
public class SysRole extends BaseEntity {

    @TableId(value = "role_id", type = IdType.AUTO)
    @ExcelColumn(value = "角色ID")
    private Long roleId;

    @NotBlank(message = "角色名称不为空")
    @Size(min = 0, max = 50, message = "角色名称长度不能超过{max}个字符")
    @ExcelColumn("角色名称")
    private String roleName;

    @NotBlank(message = "角色权限字符不为空")
    @Size(min = 0, max = 50, message = "角色权限字符长度不能超过{max}个字符")
    @ExcelColumn("角色权限字符")
    private String roleKey;

    /**
     * 数据范围
     * 1：所有数据权限
     * 2：自定义数据权限
     * 3：本部门数据权限
     * 4：本部门及以下数据权限
     * 5：仅本人数据权限
     */
    @NotBlank(message = "角色数据权限不为空")
    @ExcelColumn(value = "角色数据权限",
            convert = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer roleScope;

    @NotNull(message = "角色顺序不能为空")
    @Size(min = 0, max = 999, message = "角色顺序应在{min}-{max}之间")
    @ExcelColumn("角色顺序")
    private Integer roleSeq;

    @ExcelColumn(value = "角色状态", convert = "0=禁用,1=正常")
    private Integer roleStatus;

    @ExcelColumn(value = "角色备注")
    private String remark;

    @TableField(exist = false)
    @ExcelColumn(export = false)
    private List<Long> menuIdList = new ArrayList<>();
}
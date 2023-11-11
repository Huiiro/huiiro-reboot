package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * sys_role_menu
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    @TableId(type = IdType.INPUT)
    @ExcelColumn(value = "用户ID")
    private Long roleId;

    @ExcelColumn(value = "菜单ID")
    private Long menuId;
}
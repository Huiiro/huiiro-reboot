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
 * sys_user_role
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    @TableId(type = IdType.INPUT)
    @ExcelColumn(value = "用户ID")
    private Long userId;

    @ExcelColumn(value = "角色ID")
    private Long roleId;
}
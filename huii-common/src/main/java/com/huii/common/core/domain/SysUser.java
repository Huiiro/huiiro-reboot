package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.annotation.SensitiveColumn;
import com.huii.common.annotation.Xss;
import com.huii.common.core.model.base.BaseEntity;
import com.huii.common.strategy.SensitiveStrategy;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 实体类
 * sys_user
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(value = "user_id", type = IdType.AUTO)
    @ExcelColumn(value = "用户ID")
    private Long userId;

    @ExcelColumn(value = "部门ID")
    private Long deptId;

    @Xss(message = "账号名含有非法字符")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "账号名长度应在{min}-{max}个字符之间")
    @ExcelColumn(value = "用户名")
    private String userName;

    @Xss(message = "用户名含有非法字符")
    @Size(min = 2, max = 20, message = "用户名长度应在{min}-{max}个字符之间")
    @ExcelColumn(value = "用户姓名")
    private String nickName;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY)
    @ExcelColumn(value = "用户密码", export = false)
    private String password;

    @SensitiveColumn(strategy = SensitiveStrategy.PHONE)
    @Size(min = 0, max = 20, message = "电话号码长度不超过{max}个字符")
    @ExcelColumn(value = "用户电话")
    private String phone;

    @SensitiveColumn(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 40, message = "邮箱长度不超过{max}个字符")
    @ExcelColumn(value = "用户邮箱")
    private String email;

    @ExcelColumn(value = "用户性别", convert = "1=男,2=女,3=未知")
    private Integer sexual;

    @ExcelColumn(value = "用户头像", export = false)
    private String avatar;

    @ExcelColumn(value = "上次登录IP")
    private String loginIp;

    @ExcelColumn(value = "上次登录时间")
    private LocalDateTime loginTime;

    @TableLogic
    @ExcelColumn(value = "账号是否删除", export = false)
    private String deleteFlag;

    @ExcelColumn(value = "状态", convert = "0=禁用,1=正常")
    private String userStatus;

    @ExcelColumn(value = "备注")
    private String remark;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private SysDept dept;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private List<SysRole> roles;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Long roleId;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Long[] roleIds;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Long[] postIds;

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }
}
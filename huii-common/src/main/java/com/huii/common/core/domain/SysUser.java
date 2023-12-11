package com.huii.common.core.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.huii.common.annotation.ExcelData;
import com.huii.common.annotation.SensitiveColumn;
import com.huii.common.annotation.Xss;
import com.huii.common.convert.ExcelDataConvert;
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
@KeySequence(value = "sys_user_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysUser extends BaseEntity {

    @TableId(value = "user_id", type = IdType.INPUT)
    @ExcelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty(value = "部门ID")
    private Long deptId;

    @Xss(message = "账号名含有非法字符")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "账号名长度应在{min}-{max}个字符之间")
    @ExcelProperty(value = "用户名")
    private String userName;

    @Xss(message = "用户名含有非法字符")
    @Size(min = 2, max = 20, message = "用户名长度应在{min}-{max}个字符之间")
    @ExcelProperty(value = "用户姓名")
    private String nickName;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY)
    private String password;

    @SensitiveColumn(strategy = SensitiveStrategy.PHONE)
    @Size(min = 0, max = 20, message = "电话号码长度不超过{max}个字符")
    @ExcelProperty(value = "用户电话")
    private String phone;

    @SensitiveColumn(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 40, message = "邮箱长度不超过{max}个字符")
    @ExcelProperty(value = "用户邮箱")
    private String email;

    @ExcelProperty(value = "用户性别", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "1=男,2=女,3=未知")
    private String sexual;

    @ExcelProperty(value = "用户头像")
    private String avatar;

    @ExcelProperty(value = "上次登录IP")
    private String loginIp;

    @ExcelProperty(value = "上次登录时间")
    private LocalDateTime loginTime;

    @TableLogic
    private String deleteFlag;

    @ExcelProperty(value = "用户状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String userStatus;

    @ExcelProperty(value = "用户备注")
    private String remark;

    @TableField(exist = false)
    private SysDept dept;

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private Long roleId;

    @TableField(exist = false)
    private Long[] roleIds;

    @TableField(exist = false)
    private Long[] postIds;

    @TableField(exist = false)
    private String encrypt;

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }
}
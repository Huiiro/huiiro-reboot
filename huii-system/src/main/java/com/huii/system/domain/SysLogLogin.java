package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录日志 实体类
 * sys_log_login
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log_login")
@KeySequence(value = "sys_log_login_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysLogLogin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "login_id", type = IdType.INPUT)
    @ExcelColumn(value = "登录日志ID")
    private Long loginId;

    @ExcelColumn(value = "用户名称")
    private String loginUserName;

    @ExcelColumn(value = "登录IP")
    private String loginIp;

    @ExcelColumn(value = "登录地点")
    private String loginAddress;

    @ExcelColumn(value = "登录时间")
    private LocalDateTime loginTime;

    @ExcelColumn(value = "浏览器")
    private String loginBrowser;

    @ExcelColumn(value = "操作系统")
    private String loginOs;

    @ExcelColumn(value = "登录方式", convert = "1=账号登录,2=邮箱登录," +
            "3=手机登录,4=Github登录,5=Gitee登录")
    private Integer loginType;

    @ExcelColumn(value = "登陆结果", convert = "0=失败,1=成功")
    private String loginStatus;

    @ExcelColumn(value = "登录信息")
    private String loginMessage;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
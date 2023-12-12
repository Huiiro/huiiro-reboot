package com.huii.common.core.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SysLogLoginExportVo  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "登录日志ID")
    private Long loginId;

    @ExcelProperty(value = "用户名称")
    private String loginUserName;

    @ExcelProperty(value = "登录IP")
    private String loginIp;

    @ExcelProperty(value = "登录地点")
    private String loginAddress;

    @ExcelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ExcelProperty(value = "浏览器")
    private String loginBrowser;

    @ExcelProperty(value = "操作系统")
    private String loginOs;

    @ExcelProperty(value = "登录方式", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "1=账号登录,2=邮箱登录,3=手机登录,4=Github登录,5=Gitee登录")
    private Integer loginType;

    @ExcelProperty(value = "登陆结果", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=失败,1=成功")
    private String loginStatus;

    @ExcelProperty(value = "登录信息")
    private String loginMessage;
}

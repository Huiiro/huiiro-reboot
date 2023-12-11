package com.huii.common.core.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SysUserExcelImportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "用户序号")
    private Long userId;

    @ExcelProperty(value = "部门ID")
    private Long deptId;

    @ExcelProperty(value = "用户姓名")
    private String userName;

    @ExcelProperty(value = "用户电话")
    private String phone;

    @ExcelProperty(value = "用户邮箱")
    private String email;

    @ExcelProperty(value = "用户性别", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "1=男,2=女,3=未知")
    private String sexual;

    @ExcelProperty(value = "用户状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String userStatus;
}

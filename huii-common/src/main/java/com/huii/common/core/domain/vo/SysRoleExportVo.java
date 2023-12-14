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
public class SysRoleExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "角色ID")
    private Long roleId;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("角色权限字符")
    private String roleKey;

    @ExcelProperty(value = "角色数据权限", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String roleScope;

    @ExcelProperty("角色顺序")
    private Integer roleSeq;

    @ExcelProperty(value = "角色状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String roleStatus;

    @ExcelProperty(value = "角色备注")
    private String remark;
}

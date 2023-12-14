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
public class SysDicTypeExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "字典类型ID")
    private Long typeId;

    @ExcelProperty(value = "字典名称")
    private String typeName;

    @ExcelProperty(value = "字典类型")
    private String dicType;

    @ExcelProperty(value = "字典类型状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String typeStatus;

    @ExcelProperty(value = "字典类型备注")
    private String remark;
}

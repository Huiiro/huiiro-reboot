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
public class SysDicDataExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "字典数据ID")
    private Long dataId;

    @ExcelProperty(value = "字典类型")
    private String dataType;

    @ExcelProperty(value = "字典名称")
    private String dataName;

    @ExcelProperty(value = "字典编码")
    private String dataKey;

    @ExcelProperty(value = "字典值")
    private String dataValue;

    @ExcelProperty(value = "字典标签")
    private String dataLabel;

    @ExcelProperty("字典值顺序")
    private Integer dataSeq;

    @ExcelProperty(value = "字典警告类型")
    private String dataTypeInfo;

    @ExcelProperty(value = "字典数据状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String dataStatus;

    @ExcelProperty(value = "字典数据备注")
    private String remark;
}

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
public class SysPostExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "岗位ID")
    private Long postId;

    @ExcelProperty("岗位名称")
    private String postName;

    @ExcelProperty("岗位权限字符")
    private String postKey;

    @ExcelProperty("岗位职责")
    private String postDuty;

    @ExcelProperty(value = "岗位顺序")
    private Integer postSeq;

    @ExcelProperty(value = "岗位状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String postStatus;

    @ExcelProperty(value = "岗位备注")
    private String remark;
}

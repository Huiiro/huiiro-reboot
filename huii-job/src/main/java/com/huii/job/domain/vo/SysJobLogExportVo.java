package com.huii.job.domain.vo;

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
public class SysJobLogExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "任务名称")
    private String jobName;

    @ExcelProperty(value = "分组名称")
    private String groupName;

    @ExcelProperty(value = "调用对象")
    private String target;

    @ExcelProperty(value = "任务状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=暂停,1=执行")
    private String jobStatus;

    @ExcelProperty(value = "任务信息")
    private String jobMessage;

    @ExcelProperty(value = "错误信息")
    private String errorInfo;

    @ExcelProperty(value = "执行时间")
    private LocalDateTime beginTime;

    @ExcelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ExcelProperty(value = "任务耗时")
    private Long cost;
}

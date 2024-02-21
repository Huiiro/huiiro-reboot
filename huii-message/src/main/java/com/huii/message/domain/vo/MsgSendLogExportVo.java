package com.huii.message.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 导出日志vo
 *
 * @author huii
 */
@Data
@NoArgsConstructor
public class MsgSendLogExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "模板名称")
    private String tempName;

    @ExcelProperty(value = "推送时间")
    private LocalDateTime sendTime;

    @ExcelProperty(value = "推送状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=失败,1=成功")
    private String sendStatus;
}

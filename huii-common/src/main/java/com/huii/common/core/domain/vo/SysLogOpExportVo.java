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
public class SysLogOpExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "操作日志ID")
    private Long opId;

    @ExcelProperty(value = "操作用户名")
    private String opUserName;

    @ExcelProperty(value = "请求方法")
    private String opMethodName;

    @ExcelProperty(value = "操作类型", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "1=其他,2=查询,3=授权," +
            "4=新增,5=修改,6=删除,7=导入,8=导出,9=生成,10=清空")
    private Integer opType;

    @ExcelProperty(value = "请求时间")
    private LocalDateTime opTime;

    @ExcelProperty(value = "接口耗时(ms)")
    private Long opCostTime;

    @ExcelProperty(value = "请求IP")
    private String opIp;

    @ExcelProperty(value = "操作地点")
    private String opAddress;

    @ExcelProperty(value = "请求方式")
    private String opRequest;

    @ExcelProperty(value = "请求地址")
    private String opUrl;

    @ExcelProperty(value = "请求参数")
    private String opReqParam;

    @ExcelProperty(value = "响应参数")
    private String opResParam;

    @ExcelProperty(value = "请求结果", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=失败,1=成功")
    private String opStatus;

    @ExcelProperty(value = "标记请求", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=未标记,1=标记")
    private String opMarkFlag;

    @ExcelProperty(value = "请求结果信息")
    private String opMessage;
}

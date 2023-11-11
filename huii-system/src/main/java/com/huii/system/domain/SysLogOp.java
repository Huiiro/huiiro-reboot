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
 * 操作日志 实体类
 * sys_log_op
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log_op")
@KeySequence(value = "sys_log_op_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysLogOp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "op_id", type = IdType.INPUT)
    @ExcelColumn(value = "操作日志ID")
    private Long opId;

    @ExcelColumn(value = "操作用户名")
    private String opUserName;

    @ExcelColumn(value = "请求方法")
    private String opMethodName;

    @ExcelColumn(value = "操作类型", convert = "1=其他,2=查询,3=授权," +
            "4=新增,5=修改,6=删除,7=导入,8=导出,9=生成,10=清空")
    private Integer opType;

    @ExcelColumn(value = "请求时间")
    private LocalDateTime opTIme;

    @ExcelColumn(value = "接口耗时(ms)")
    private Long opCostTime;

    @ExcelColumn(value = "请求IP")
    private String opIp;

    @ExcelColumn(value = "操作地点")
    private String opAddress;

    @ExcelColumn(value = "请求方式")
    private String opRequest;

    @ExcelColumn(value = "请求地址")
    private String opUrl;

    @ExcelColumn(value = "请求参数", export = false)
    private String opReqParam;

    @ExcelColumn(value = "响应参数", export = false)
    private String opResParam;

    @ExcelColumn(value = "请求结果", convert = "0=失败,1=成功")
    private String opStatus;

    @ExcelColumn(value = "标记请求,", convert = "0=未标记,1=标记")
    private String opMarkFlag;

    @ExcelColumn(value = "请求结果信息")
    private String opMessage;

    @ExcelColumn(export = false)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
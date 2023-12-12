package com.huii.common.core.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.ExcelData;
import com.huii.common.convert.ExcelDataConvert;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 字典数据 实体类
 * sys_dic_data
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dic_data")
@KeySequence(value = "sys_dic_data_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysDicData extends BaseEntity {

    @TableId(value = "data_id", type = IdType.INPUT)
    @ExcelProperty(value = "字典数据ID")
    private Long dataId;

    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 50, message = "字典键值长度不能超过{max}个字符")
    @ExcelProperty(value = "字典类型")
    private String dataType;

    @Size(min = 0, max = 50, message = "字典键值长度不能超过{max}个字符")
    @ExcelProperty(value = "字典名称")
    private String dataName;

    @NotBlank(message = "字典编码不能为空")
    @Size(min = 0, max = 50, message = "字典键值长度不能超过{max}个字符")
    @ExcelProperty(value = "字典编码")
    private String dataKey;

    @NotBlank(message = "字典值不能为空")
    @Size(min = 0, max = 500, message = "字典键值长度不能超过{max}个字符")
    @ExcelProperty(value = "字典值")
    private String dataValue;

    @Size(min = 0, max = 50, message = "字典标签长度不能超过{max}个字符")
    @ExcelProperty(value = "字典标签")
    private String dataLabel;

    @NotNull(message = "字典值顺序不为空")
    @Min(value = 0, message = "字典值顺序应大于等于 {value}")
    @Max(value = 999, message = "字典值顺序应小于等于 {value}")
    @ExcelProperty("字典值顺序")
    private Integer dataSeq;

    @Size(min = 0, max = 255, message = "字典警告类型长度不能超过{max}个字符")
    @ExcelProperty(value = "字典警告类型")
    private String dataTypeInfo;

    @ExcelProperty(value = "字典数据状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String dataStatus;

    @ExcelProperty(value = "字典数据备注")
    private String remark;
}
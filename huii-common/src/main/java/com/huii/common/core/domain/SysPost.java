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
 * 岗位 实体类
 * sys_post
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@KeySequence(value = "sys_post_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysPost extends BaseEntity {

    @TableId(value = "post_id", type = IdType.INPUT)
    @ExcelProperty(value = "岗位ID")
    private Long postId;

    @NotBlank(message = "岗位名称不为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过{max}个字符")
    @ExcelProperty("岗位名称")
    private String postName;

    @NotBlank(message = "岗位权限字符不为空")
    @Size(min = 0, max = 50, message = "岗位权限字符长度不能超过{max}个字符")
    @ExcelProperty("岗位权限字符")
    private String postKey;

    @NotBlank(message = "岗位职责不为空")
    @Size(min = 0, max = 255, message = "岗位职责长度不能超过{max}个字符")
    @ExcelProperty("岗位职责")
    private String postDuty;

    @NotNull(message = "岗位顺序不为空")
    @Min(value = 0, message = "岗位顺序应大于等于 {value}")
    @Max(value = 999, message = "岗位顺序应小于等于 {value}")
    @ExcelProperty(value = "岗位顺序")
    private Integer postSeq;

    @ExcelProperty(value = "岗位状态", converter = ExcelDataConvert.class)
    @ExcelData(readConverterExp = "0=禁用,1=正常")
    private String postStatus;

    @ExcelProperty(value = "岗位备注")
    private String remark;
}
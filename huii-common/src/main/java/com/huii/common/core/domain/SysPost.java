package com.huii.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class SysPost extends BaseEntity {

    @TableId(value = "post_id", type = IdType.AUTO)
    @ExcelColumn(value = "岗位ID")
    private Long postId;

    @NotBlank(message = "岗位名称不为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过{max}个字符")
    @ExcelColumn("岗位名称")
    private String postName;

    @NotBlank(message = "岗位权限字符不为空")
    @Size(min = 0, max = 50, message = "岗位权限字符长度不能超过{max}个字符")
    @ExcelColumn("岗位权限字符")
    private String postKey;

    @NotBlank(message = "岗位职责不为空")
    @Size(min = 0, max = 255, message = "岗位职责长度不能超过{max}个字符")
    @ExcelColumn("岗位职责")
    private String postDuty;

    @NotNull(message = "岗位顺序不为空")
    @Size(min = 0, max = 999, message = "岗位顺序应在{min}-{max}之间")
    @ExcelColumn(value = "岗位顺序")
    private Integer postSeq;

    @ExcelColumn(value = "岗位状态", convert = "0=禁用,1=正常")
    private Integer postStatus;

    @ExcelColumn(value = "岗位备注")
    private String remark;
}
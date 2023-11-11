package com.huii.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.ExcelColumn;
import com.huii.common.annotation.Xss;

import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 消息 实体类
 * sys_notice
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@KeySequence(value = "sys_notice_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysNotice extends BaseEntity {

    @TableId(value = "notice_id", type = IdType.INPUT)
    @ExcelColumn(value = "公告ID")
    private Long noticeId;

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 255, message = "公告标题不能超过{max}个字符")
    @ExcelColumn(value = "公告标题")
    private String noticeTitle;

    @ExcelColumn(value = "公告内容")
    private String noticeContent;

    @ExcelColumn(value = "公告类型")
    private Integer noticeType;

    @ExcelColumn(value = "公告状态")
    private String noticeStatus;

    @ExcelColumn("公告备注")
    private String remark;
}
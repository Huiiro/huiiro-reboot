package com.huii.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.annotation.Xss;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 系统消息 实体类
 * sys_message
 *
 * @author huii
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message")
@KeySequence(value = "sys_message_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysMessage extends BaseEntity {

    @TableId(value = "message_id", type = IdType.INPUT)
    @ExcelProperty(value = "消息ID")
    private Long messageId;

    @ExcelProperty(value = "发送者")
    private Long sendId;

    @ExcelProperty(value = "接收者")
    private Long receiveId;

    @Xss(message = "消息内容不能包含脚本字符")
    @NotBlank(message = "最少包含一个字符")
    @Size(min = 0, max = 499, message = "消息内容不能超过{max}个字符")
    @ExcelProperty(value = "消息内容")
    private String message;

    @ExcelProperty(value = "消息状态")
    private String messageStatus;

    @ExcelProperty(value = "消息类型")
    private String messageType;

    @ExcelProperty(value = "是否已读")
    private String messageRead;
}

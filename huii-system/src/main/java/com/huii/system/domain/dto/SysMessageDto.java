package com.huii.system.domain.dto;

import com.huii.common.annotation.Xss;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageDto extends BaseEntity {

    private Long messageId;

    private Long sendId;

    @NotBlank(message = "消息接收对象不为空")
    private String receiveId;

    @Xss(message = "消息内容不能包含脚本字符")
    @NotBlank(message = "最少包含一个字符")
    @Size(min = 0, max = 499, message = "消息内容不能超过{max}个字符")
    private String message;

    private String messageStatus;

    private String messageType;

    private String messageRead;
}

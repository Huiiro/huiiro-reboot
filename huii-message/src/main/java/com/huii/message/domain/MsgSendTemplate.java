package com.huii.message.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 发送模板实体类
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_send_template")
@KeySequence(value = "msg_send_template_id_seq", dbType = DbType.POSTGRE_SQL)
public class MsgSendTemplate extends BaseEntity {

    /**
     * ID
     */
    @TableId(value = "temp_id", type = IdType.INPUT)
    private Long tempId;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不为空")
    private String tempName;

    /**
     * 模板参数，','分割
     */
    private String tempParams;

    /**
     * 发送类型，1--sms，2--mail
     */
    private String sendType;

    /**
     * 发送对象，','分割
     * 发送对象为用户ID，为0时代表发送全部对象，为a-b时，代表发送id在a-b的用户
     */
    @NotBlank(message = "发送的对象不能为空")
    private String sendTargets;

    /**
     * 发送时间
     * 设置定时任务
     */
    private LocalDateTime sendTime;

    /**
     * 发送状态，1--待发送，2--已发送，3--发送失败
     */
    private String sendStatus;

    /**
     * 模板备注
     */
    private String remark;

}

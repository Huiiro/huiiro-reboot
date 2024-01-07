package com.huii.message.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板实体类
 *
 * @author huii
 * @date 2024-01-07T15:31:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_mail_template")
@KeySequence(value = "msg_mail_template_id_seq", dbType = DbType.POSTGRE_SQL)
public class MsgMailTemplate extends BaseEntity {

    /**
     * ID
     */
    @TableId(value = "mail_temp_id", type = IdType.INPUT)
    private Long mailTempId;

    /**
     * 类型(1--text,2--html,3--attach)
     */
    @NotBlank(message = "类型不为空")
    private String mailType;

    /**
     * 邮件主题
     */
    @NotBlank(message = "邮件主题不为空")
    private String mailSubject;

    /**
     * 邮件内容
     */
    @NotBlank(message = "邮件内容不为空")
    private String mailContent;

    /**
     * 邮件附件
     */
    private String mailAttachFile;

    /**
     * 模板名称 mailTargets
     */
    private String tempName;

    /**
     * 模板备注
     */
    private String remark;

}

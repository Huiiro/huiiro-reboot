package com.huii.message.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private String sendTempParams;

    /**
     * 模板名称（需要加载的发送模板）
     * 例如本地邮件模板，sms运营商的模板
     */
    @NotBlank(message = "待加载模板名称不为空")
    private String sendTempName;

    /**
     * 发送类型，1--sms，2--mail， 3--add more types
     */
    @NotBlank(message = "请选择发送类型")
    private String sendType;

    /**
     * 发送对象，','分割
     * 发送对象为用户ID，为0时代表发送全部对象，为a-b时，代表发送id在a-b的用户
     */
    private String sendTargets;

    /**
     * 订阅ID（订阅用户组）
     */
    private Long subId;

    /**
     * 模板备注
     */
    private String remark;
}

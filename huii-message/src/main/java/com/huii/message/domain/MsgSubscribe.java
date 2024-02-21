package com.huii.message.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订阅实体类
 *
 * @author huii
 * @date 2024-02-20T21:18:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_subscribe")
@KeySequence(value = "msg_subscribe_id_seq", dbType = DbType.POSTGRE_SQL)
public class MsgSubscribe extends BaseEntity {

    /**
     * 订阅ID
     */
    @TableId(value = "sub_id", type = IdType.INPUT)
    private Long subId;

    /**
     * 订阅名称
     */
    @Size(min = 0, max = 30, message = "订阅名称不能超过{max}个字符")
    @NotBlank(message = "订阅名称不为空")
    private String subName;

    /**
     * 订阅描述
     */
    @Size(min = 0, max = 255, message = "订阅描述不能超过{max}个字符")
    @NotBlank(message = "订阅描述不为空")
    private String subDesc;

    /**
     * 订阅状态
     */
    private String subStatus;

    /**
     * 订阅备注
     */
    private String remark;

}

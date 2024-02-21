package com.huii.message.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送日志实体类
 *
 * @author huii
 */
@Data
@TableName("msg_send_log")
@KeySequence(value = "msg_send_log_id_seq", dbType = DbType.POSTGRE_SQL)
public class MsgSendLog implements Serializable {

    /**
     * 日志ID
     */
    @TableId(value = "log_id", type = IdType.INPUT)
    private Long logId;

    /**
     * 模板名称
     */
    private String tempName;

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 发送对象
     */
    private String sendTarget;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送状态
     */
    private String sendStatus;

    /**
     * 错误信息
     */
    private String errInfo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}

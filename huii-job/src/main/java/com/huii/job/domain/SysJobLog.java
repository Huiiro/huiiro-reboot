package com.huii.job.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务日志实体类
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Data
@TableName("sys_job_log")
@KeySequence(value = "sys_job_log_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysJobLog implements Serializable {

    /**
     * logId
     */
    @TableId(value = "log_id", type = IdType.INPUT)
    private Long logId;

    /**
     * jobName
     */
    private String jobName;

    /**
     * groupName
     */
    private String groupName;

    /**
     * target
     */
    private String target;

    /**
     * jobStatus
     */
    private String jobStatus;

    /**
     * jobMessage
     */
    private String jobMessage;

    /**
     * errorInfo
     */
    private String errorInfo;

    /**
     * beginTime
     */
    private LocalDateTime beginTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;

    /**
     * cost
     */
    private Long cost;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}

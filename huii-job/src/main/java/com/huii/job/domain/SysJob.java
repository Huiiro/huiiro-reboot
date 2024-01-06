package com.huii.job.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.huii.common.core.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务实体类
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_job")
@KeySequence(value = "sys_job_id_seq", dbType = DbType.POSTGRE_SQL)
public class SysJob extends BaseEntity {

    /**
     * jobId
     */
    @TableId(value = "job_id", type = IdType.INPUT)
    private Long jobId;

    /**
     * jobName
     */
    private String jobName;

    /**
     * groupName
     */
    private String groupName;

    /**
     * cron
     */
    private String cron;

    /**
     * target
     */
    private String target;

    /**
     * jobStatus
     */
    private String jobStatus;

    /**
     * concurrentStatus
     */
    private String concurrentStatus;

    /**
     * misfirePolicy
     */
    private Integer misfirePolicy;

    /**
     * remark
     */
    private String remark;

    public SysJob(SysJob sysJob) {
        this.jobId = sysJob.getJobId();
        this.jobName = sysJob.getJobName();
        this.groupName = sysJob.getGroupName();
        this.cron = sysJob.getCron();
        this.target = sysJob.getTarget();
        this.jobStatus = sysJob.getJobStatus();
        this.concurrentStatus = sysJob.getConcurrentStatus();
        this.misfirePolicy = sysJob.getMisfirePolicy();
        this.remark = sysJob.getRemark();
    }
}

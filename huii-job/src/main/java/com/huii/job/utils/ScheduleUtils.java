package com.huii.job.utils;

import com.huii.common.constants.JobConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.exception.ServiceException;
import com.huii.job.core.ConcurrentJobExecution;
import com.huii.job.core.SimpleJobExecution;
import com.huii.job.domain.SysJob;
import org.apache.commons.lang3.ObjectUtils;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author huii
 */
public class ScheduleUtils {

    /**
     * 获取执行的任务的类型，是否并发
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob) {
        boolean isConcurrent = SystemConstants.STATUS_1.equals(sysJob.getConcurrentStatus());
        return isConcurrent ? ConcurrentJobExecution.class : SimpleJobExecution.class;
    }

    /**
     * 创建任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob sysJob) throws Exception {
        Class<? extends Job> jobClazz = getQuartzJobClass(sysJob);
        Long id = sysJob.getJobId();
        String group = sysJob.getGroupName();

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCron());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(sysJob, cronScheduleBuilder);

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(id, group)).withSchedule(cronScheduleBuilder).build();

        JobDetail detail = JobBuilder.newJob(jobClazz).withIdentity(getJobKey(id, group)).build();
        detail.getJobDataMap().put(JobConstants.TASK_PROPERTIES, sysJob);

        if (scheduler.checkExists(getJobKey(id, group))) {
            scheduler.deleteJob(getJobKey(id, group));
        }
        if (ObjectUtils.isNotEmpty(CronUtils.getNextExecution(sysJob.getCron()))) {
            scheduler.scheduleJob(detail, trigger);
        }
        if (JobConstants.JOB_STATUS_PAUSE == Integer.parseInt(sysJob.getJobStatus())) {
            scheduler.pauseJob(getJobKey(id, group));
        }
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long id, String group) {
        return JobKey.jobKey(JobConstants.TASK_CLASS_NAME + id, group);
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long id, String group) {
        return TriggerKey.triggerKey(JobConstants.TASK_CLASS_NAME + id, group);
    }

    /**
     * 构建定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob sysJob, CronScheduleBuilder cb) {
        return switch (sysJob.getMisfirePolicy()) {
            case JobConstants.MISFIRE_DEFAULT -> cb;
            case JobConstants.MISFIRE_IGNORE_MISFIRES -> cb.withMisfireHandlingInstructionIgnoreMisfires();
            case JobConstants.MISFIRE_FIRE_AND_PROCEED -> cb.withMisfireHandlingInstructionFireAndProceed();
            case JobConstants.MISFIRE_DO_NOTHING -> cb.withMisfireHandlingInstructionDoNothing();
            default -> throw new ServiceException("定时任务执行策略获取失败");
        };
    }
}

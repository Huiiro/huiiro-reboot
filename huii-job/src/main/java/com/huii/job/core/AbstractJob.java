package com.huii.job.core;

import com.huii.common.constants.JobConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.utils.SpringUtils;
import com.huii.job.domain.SysJob;
import com.huii.job.domain.SysJobLog;
import com.huii.job.service.SysJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * quartz job抽象类
 *
 * @author huii
 */
@Slf4j
public abstract class AbstractJob implements Job {

    private static final ThreadLocal<LocalDateTime> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        SysJob sysJob = new SysJob((SysJob) context.getMergedJobDataMap().get(JobConstants.TASK_PROPERTIES));
        try {
            before(context, sysJob);
            doExecute(context, sysJob);
            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常: ", e);
            after(context, sysJob, e);
        }
    }

    private void before(JobExecutionContext context, SysJob sysJob) {
        THREAD_LOCAL.set(LocalDateTime.now());
    }

    private void after(JobExecutionContext context, SysJob sysJob, Exception e) {
        LocalDateTime beginTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setGroupName(sysJob.getGroupName());
        sysJobLog.setTarget(sysJob.getTarget());
        sysJobLog.setBeginTime(beginTime);
        sysJobLog.setEndTime(LocalDateTime.now());
        long cost = Duration.between(sysJobLog.getBeginTime(), sysJobLog.getEndTime()).toMillis();
        sysJobLog.setCost(cost);
        if (e != null) {
            sysJobLog.setJobStatus(SystemConstants.STATUS_0);
            sysJobLog.setErrorInfo(e.getMessage().substring(0, Math.min(e.getMessage().length(), 2000)));
        } else {
            sysJobLog.setJobStatus(SystemConstants.STATUS_1);
        }
        SpringUtils.getBean(SysJobLogService.class).insertSysJobLog(sysJobLog);
    }

    /**
     * 抽象重载方法
     *
     * @param context context
     * @param sysJob  job
     * @throws Exception e
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}

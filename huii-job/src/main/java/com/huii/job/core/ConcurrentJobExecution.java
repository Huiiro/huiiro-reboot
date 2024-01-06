package com.huii.job.core;

import com.huii.job.domain.SysJob;
import com.huii.job.utils.InvokeUtils;
import org.quartz.JobExecutionContext;

/**
 * 多线程任务执行处理
 *
 * @author huii
 */
public class ConcurrentJobExecution extends AbstractJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        InvokeUtils.invokeMethod(sysJob);
    }
}

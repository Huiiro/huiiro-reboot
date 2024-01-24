package com.huii.job.core;

import com.huii.job.domain.SysJob;
import com.huii.job.utils.InvokeUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 简单任务执行处理
 *
 * @author huii
 */
@DisallowConcurrentExecution
public class SimpleJobExecution extends AbstractJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        InvokeUtils.invokeMethod(sysJob);
    }
}

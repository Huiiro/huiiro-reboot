package com.huii.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.common.constants.JobConstants;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.utils.PageParamUtils;
import com.huii.job.domain.SysJob;
import com.huii.job.exception.JobException;
import com.huii.job.mapper.SysJobMapper;
import com.huii.job.service.SysJobService;
import com.huii.job.utils.CronUtils;
import com.huii.job.utils.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务服务层实现
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {

    private final Scheduler scheduler;
    private final SysJobMapper sysJobMapper;

    @PostConstruct
    public void init() throws Exception {
        scheduler.clear();
        List<SysJob> list = sysJobMapper.selectList(null);
        for (SysJob job : list) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public Page selectSysJobList(SysJob sysJob, PageParam pageParam) {
        IPage<SysJob> iPage = new PageParamUtils<SysJob>().getPageInfo(pageParam);
        return new Page(this.page(iPage, wrapperBuilder(sysJob)));
    }

    @Override
    public SysJob selectSysJobById(Long id) {
        return sysJobMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(SysJob sysJob) throws SchedulerException {
        sysJob.setJobStatus(String.valueOf(JobConstants.JOB_STATUS_PAUSE));
        int updated = sysJobMapper.updateById(sysJob);
        if (updated > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(sysJob.getJobId(), sysJob.getGroupName()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeJob(SysJob sysJob) throws SchedulerException {
        sysJob.setJobStatus(String.valueOf(JobConstants.JOB_STATUS_OK));
        int updated = sysJobMapper.updateById(sysJob);
        if (updated > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(sysJob.getJobId(), sysJob.getGroupName()));
        }
    }

    @Override
    public void runJob(SysJob sysJob) throws SchedulerException {
        SysJob before = selectSysJobById(sysJob.getJobId());
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(JobConstants.TASK_PROPERTIES, before);
        JobKey jobKey = ScheduleUtils.getJobKey(sysJob.getJobId(), sysJob.getGroupName());
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey, dataMap);
        }
    }

    @Override
    public void checkInsert(SysJob sysJob) {
        if(!CronUtils.isValid(sysJob.getCron())) {
            throw new JobException("cron表达式不正确");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSysJob(SysJob sysJob) throws Exception {
        sysJob.setJobStatus(String.valueOf(JobConstants.JOB_STATUS_PAUSE));
        int insert = sysJobMapper.insert(sysJob);
        if (insert > 0) {
            ScheduleUtils.createScheduleJob(scheduler, sysJob);
        }
    }

    @Override
    public void checkUpdate(SysJob sysJob) {
        if(!CronUtils.isValid(sysJob.getCron())) {
            throw new JobException("cron表达式不正确");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysJob(SysJob sysJob) throws Exception {
        SysJob before = selectSysJobById(sysJob.getJobId());
        int updated = sysJobMapper.updateById(sysJob);
        if (updated > 0) {
            JobKey jobKey = ScheduleUtils.getJobKey(sysJob.getJobId(), before.getGroupName());
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
            ScheduleUtils.createScheduleJob(scheduler, sysJob);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysJobStatus(SysJob sysJob) throws SchedulerException {
        String jobStatus = sysJob.getJobStatus();
        if (String.valueOf(JobConstants.JOB_STATUS_PAUSE).equals(jobStatus)) {
            pauseJob(sysJob);
        } else if (String.valueOf(JobConstants.JOB_STATUS_OK).equals(jobStatus)) {
            resumeJob(sysJob);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysJob(Long[] ids) throws SchedulerException {
        for (Long id : ids) {
            SysJob sysJob = sysJobMapper.selectById(id);
            int deleted = sysJobMapper.deleteById(sysJob);
            if(deleted > 0) {
                scheduler.deleteJob(ScheduleUtils.getJobKey(sysJob.getJobId(), sysJob.getGroupName()));
            }
        }
    }

    private LambdaQueryWrapper<SysJob> wrapperBuilder(SysJob sysJob) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ObjectUtils.isNotEmpty(sysJob.getJobId()), SysJob::getJobId, sysJob.getJobId())
                .like(ObjectUtils.isNotEmpty(sysJob.getJobName()), SysJob::getJobName, sysJob.getJobName())
                .like(ObjectUtils.isNotEmpty(sysJob.getGroupName()), SysJob::getGroupName, sysJob.getGroupName())
                .eq(ObjectUtils.isNotEmpty(sysJob.getJobStatus()), SysJob::getJobStatus, sysJob.getJobStatus())
                .eq(ObjectUtils.isNotEmpty(sysJob.getConcurrentStatus()), SysJob::getConcurrentStatus, sysJob.getConcurrentStatus())
                .eq(ObjectUtils.isNotEmpty(sysJob.getMisfirePolicy()), SysJob::getMisfirePolicy, sysJob.getMisfirePolicy())
                .orderByAsc(SysJob::getCreateTime);
        return wrapper;
    }
}
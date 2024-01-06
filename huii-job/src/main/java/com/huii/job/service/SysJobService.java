package com.huii.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.job.domain.SysJob;
import org.quartz.SchedulerException;

/**
 * 任务服务层接口
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
public interface SysJobService extends IService<SysJob> {

    /**
     * 查询任务分页
     *
     * @param sysJob    sysJob
     * @param pageParam pageParam
     * @return page
     */
    Page selectSysJobList(SysJob sysJob, PageParam pageParam);

    /**
     * 查询任务
     *
     * @param id id
     * @return sysJob
     */
    SysJob selectSysJobById(Long id);

    /**
     * 暂停任务
     *
     * @param sysJob sysJob
     */
    void pauseJob(SysJob sysJob) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param sysJob sysJob
     */
    void resumeJob(SysJob sysJob) throws SchedulerException;

    /**
     * 运行任务
     * @param sysJob sysJob
     */
    void runJob(SysJob sysJob) throws SchedulerException;

    /**
     * 校验添加任务数据
     *
     * @param sysJob sysJob
     */
    void checkInsert(SysJob sysJob);

    /**
     * 添加任务
     *
     * @param sysJob sysJob
     */
    void insertSysJob(SysJob sysJob) throws Exception;

    /**
     * 校验修改任务数据
     *
     * @param sysJob sysJob
     */
    void checkUpdate(SysJob sysJob);

    /**
     * 修改任务
     *
     * @param sysJob sysJob
     */
    void updateSysJob(SysJob sysJob) throws Exception;

    /**
     * 修改任务状态
     *
     * @param sysJob sysJob
     */
    void updateSysJobStatus(SysJob sysJob) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param ids ids
     */
    void deleteSysJob(Long[] ids) throws SchedulerException;
}
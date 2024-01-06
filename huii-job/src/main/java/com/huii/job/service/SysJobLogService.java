package com.huii.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.job.domain.SysJobLog;

import java.util.List;

/**
 * 任务日志服务层接口
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
public interface SysJobLogService extends IService<SysJobLog> {

    /**
     * 查询任务日志分页
     *
     * @param sysJobLog sysJobLog
     * @param pageParam pageParam
     * @return page
     */
    Page selectSysJobLogList(SysJobLog sysJobLog, PageParam pageParam);

    /**
     * 查询任务日志集合
     *
     * @param sysJobLog sysJobLog
     * @return list
     */
    List<SysJobLog> selectSysJobLogList(SysJobLog sysJobLog);

    /**
     * 查询任务日志
     *
     * @param id id
     * @return sysJobLog
     */
    SysJobLog selectSysJobLogById(Long id);

    /**
     * 新增任务日志
     *
     * @param sysJobLog sysJobLog
     */
    void insertSysJobLog(SysJobLog sysJobLog);

    /**
     * 删除任务日志
     *
     * @param ids ids
     */
    void deleteSysJobLog(Long[] ids);

    /**
     * 删除全部任务日志
     */
    void deleteSysJobLogAll();

}
package com.huii.job.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.job.domain.SysJob;
import com.huii.job.service.SysJobService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 任务服务层实现
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Validated
@RestController
@RequestMapping("/system/job")
@RequiredArgsConstructor
public class SysJobController extends BaseController {

    private final SysJobService sysJobService;

    /**
     * 查询任务分页
     */
    @GetMapping("/list")
    public R<Page> getList(SysJob sysJob, PageParam pageParam) {
        Page page = sysJobService.selectSysJobList(sysJob, pageParam);
        return R.ok(page);
    }

    /**
     * 查询任务
     */
    @GetMapping(value = "/{id}")
    public R<SysJob> getSysJob(@PathVariable Long id) {
        return R.ok(sysJobService.selectSysJobById(id));
    }

    /**
     * 添加任务
     */
    @PreAuthorize("@ap.hasAuth('system:job:add')")
    @PostMapping("/insert")
    @Log(value = "添加任务", opType = OpType.INSERT)
    public R<Void> insertSysJob(@Validated @RequestBody SysJob sysJob) throws Exception {
        sysJobService.checkInsert(sysJob);
        sysJobService.insertSysJob(sysJob);
        return saveSuccess();
    }

    /**
     * 更新任务
     */
    @PreAuthorize("@ap.hasAuth('system:job:edit')")
    @PostMapping("/update")
    @Log(value = "更新任务", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateSysJob(@Validated @RequestBody SysJob sysJob) throws Exception {
        sysJobService.checkUpdate(sysJob);
        sysJobService.updateSysJob(sysJob);
        return updateSuccess();
    }

    /**
     * 更新任务执行状态
     */
    @PreAuthorize("@ap.hasAuth('system:job:edit')")
    @PostMapping("/update/status")
    @Log(value = "更新任务执行状态", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateSysJobStatus(@Validated @RequestBody SysJob sysJob) throws Exception {
        SysJob before = sysJobService.getById(sysJob.getJobId());
        before.setJobStatus(sysJob.getJobStatus());
        sysJobService.updateSysJobStatus(before);
        return updateSuccess();
    }

    /**
     * 删除任务
     */
    @PreAuthorize("@ap.hasAuth('system:job:delete')")
    @PostMapping("/delete")
    @Log(value = "删除任务", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteSysJob(@RequestBody Long[] ids) throws SchedulerException {
        sysJobService.deleteSysJob(ids);
        return deleteSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:job:edit')")
    @PostMapping("/run")
    @Log(value = "执行任务", opType = OpType.DELETE)
    public R<Void> runSysJob(@Validated @RequestBody SysJob sysJob) throws SchedulerException {
        sysJobService.runJob(sysJob);
        return R.ok("执行成功");
    }
}
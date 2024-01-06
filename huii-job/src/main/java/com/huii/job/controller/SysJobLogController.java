package com.huii.job.controller;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.job.domain.SysJobLog;
import com.huii.job.domain.vo.SysJobLogExportVo;
import com.huii.job.service.SysJobLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 任务日志控制层实现
 *
 * @author huii
 * @date 2024-01-06T16:59:36
 */
@Validated
@RestController
@RequestMapping("/system/job/log")
@RequiredArgsConstructor
public class SysJobLogController extends BaseController {

    private final SysJobLogService sysJobLogService;

    /**
     * 查询任务日志分页
     */
    @GetMapping("/list")
    public R<Page> getList(SysJobLog sysJobLog, PageParam pageParam) {
        Page page = sysJobLogService.selectSysJobLogList(sysJobLog, pageParam);
        return R.ok(page);
    }

    @PreAuthorize("@ap.hasAuth('system:job:log:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出任务日志", opType = OpType.EXPORT)
    public void exportJobLog(SysJobLog sysJobLog, HttpServletResponse response) {
        List<SysJobLog> list = sysJobLogService.selectSysJobLogList(sysJobLog);
        List<SysJobLogExportVo> vos = BeanCopyUtils.copyList(list, SysJobLogExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysJobLogExportVo.class, response);
    }

    /**
     * 查询任务日志
     */
    @GetMapping(value = "/{id}")
    public R<SysJobLog> getSysJobLog(@PathVariable Long id) {
        return R.ok(sysJobLogService.selectSysJobLogById(id));
    }

    /**
     * 删除任务日志
     */
    @PreAuthorize("@ap.hasAuth('system:job:log:delete')")
    @PostMapping("/delete")
    @Log(value = "删除任务日志", opType = OpType.DELETE)
    public R<Void> deleteSysJobLog(@RequestBody Long[] ids) {
        sysJobLogService.deleteSysJobLog(ids);
        return deleteSuccess();
    }

    /**
     * 删除全部任务日志
     */
    @PreAuthorize("@ap.hasAuth('system:job:log:delete')")
    @PostMapping("/delete/all")
    @Log(value = "删除任务日志", opType = OpType.DELETE)
    public R<Void> deleteSysJobLogAll() {
        sysJobLogService.deleteSysJobLogAll();
        return deleteSuccess();
    }
}
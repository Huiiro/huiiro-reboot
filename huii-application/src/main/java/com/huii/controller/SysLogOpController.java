package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.vo.SysLogOpExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.system.domain.SysLogOp;
import com.huii.system.service.SysLogOpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/system/log/op")
@RequiredArgsConstructor
public class SysLogOpController extends BaseController {

    private final SysLogOpService sysLogOpService;

    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/list")
    public R<Page> getList(SysLogOp sysLogOp, PageParam pageParam) {
        Page page = sysLogOpService.selectSysLogOpList(sysLogOp, pageParam);
        return R.ok(page);
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出日志", opType = OpType.EXPORT)
    public void exportLopOp(SysLogOp sysLogOp, HttpServletResponse response) {
        List<SysLogOp> list = sysLogOpService.selectSysLogOpList(sysLogOp);
        List<SysLogOpExportVo> vos = BeanCopyUtils.copyList(list, SysLogOpExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysLogOpExportVo.class, response);
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update/flag")
    public R<Void> updateLogOpFlagStatus(@RequestBody SysLogOp sysLogOp) {
        sysLogOpService.updateLogOpFlagStatus(sysLogOp);
        return R.ok();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete")
    @Log(value = "删除日志")
    public R<Void> deleteLogOp(@RequestBody Long[] ids) {
        sysLogOpService.removeBatchByIds(ids);
        return deleteSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete/all")
    @Log(value = "删除日志")
    public R<Void> deleteAll() {
        sysLogOpService.removeAll();
        return deleteSuccess();
    }
}

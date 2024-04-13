package com.huii.controller.monitor;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.vo.SysLogLoginExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.system.domain.SysLogLogin;
import com.huii.system.service.SysLogLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统登录日志控制层
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/system/log/login")
@RequiredArgsConstructor
public class SysLogLoginController extends BaseController {

    private final SysLogLoginService sysLogLoginService;

    @GetMapping("/list")
    public R<Page> getList(SysLogLogin sysLogLogin, PageParam pageParam) {
        Page page = sysLogLoginService.selectSysLogLoginList(sysLogLogin, pageParam);
        return R.ok(page);
    }

    @PreAuthorize("@ap.hasAuth('system:log:login:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出登录日志", opType = OpType.EXPORT)
    public void exportLopLogin(SysLogLogin sysLogLogin, HttpServletResponse response) {
        List<SysLogLogin> list = sysLogLoginService.selectSysLogLoginList(sysLogLogin);
        List<SysLogLoginExportVo> vos = BeanCopyUtils.copyList(list, SysLogLoginExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysLogLoginExportVo.class, response);
    }

    @PreAuthorize("@ap.hasAuth('system:log:login:delete')")
    @PostMapping("/delete")
    @Log(value = "删除登录日志", opType = OpType.DELETE)
    public R<Void> deleteLogLogin(@RequestBody Long[] ids) {
        sysLogLoginService.removeBatchByIds(ids);
        return deleteSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:log:login:delete:all')")
    @PostMapping("/delete/all")
    @Log(value = "删除全部登录日志", opType = OpType.DELETE)
    public R<Void> deleteAll() {
        sysLogLoginService.removeAll();
        return deleteSuccess();
    }
}

package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.domain.SysConfig;
import com.huii.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController extends BaseController {

    private final SysConfigService sysConfigService;

    @GetMapping("/list")
    public R<Page> getList(SysConfig sysConfig, PageParam pageParam) {
        Page page = sysConfigService.selectConfigList(sysConfig, pageParam);
        return R.ok(page);
    }

    @GetMapping(value = "/{id}")
    public R<SysConfig> getRole(@PathVariable Long id) {
        return R.ok(sysConfigService.selectConfigById(id));
    }


    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加配置", opType = OpType.INSERT)
    public R<Void> insertConfig(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.checkInsert(sysConfig);
        sysConfigService.insertConfig(sysConfig);
        return saveSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update")
    @Log(value = "更新配置", opType = OpType.UPDATE)
    public R<Void> updateConfig(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.checkUpdate(sysConfig);
        sysConfigService.updateConfig(sysConfig);
        return saveSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete")
    @Log(value = "删除配置", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteConfig(@RequestBody Long[] ids) {
        sysConfigService.deleteConfig(ids);
        return deleteSuccess();
    }
}

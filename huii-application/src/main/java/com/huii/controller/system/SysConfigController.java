package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
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

    @PreAuthorize("@ap.hasAuth('system:config:edit')")
    @GetMapping("/refresh")
    public R<Void> refreshCache() {
        sysConfigService.refreshCache();
        return R.ok(MessageUtils.message(ResType.SYS_CACHE_REFRESH_SUCCESS.getI18n()));
    }

    /**
     * 获取配置列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysConfig sysConfig, PageParam pageParam) {
        Page page = sysConfigService.selectConfigList(sysConfig, pageParam);
        return R.ok(page);
    }

    /**
     * 获取列表
     */
    @GetMapping(value = "/{id}")
    public R<SysConfig> getConfig(@PathVariable Long id) {
        return R.ok(sysConfigService.selectConfigById(id));
    }

    /**
     * 添加配置
     */
    @PreAuthorize("@ap.hasAuth('system:config:add')")
    @PostMapping("/insert")
    @Log(value = "添加配置", opType = OpType.INSERT)
    public R<Void> insertConfig(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.checkInsert(sysConfig);
        sysConfigService.insertConfig(sysConfig);
        return saveSuccess();
    }

    /**
     * 更新配置
     */
    @PreAuthorize("@ap.hasAuth('system:config:edit')")
    @PostMapping("/update")
    @Log(value = "更新配置", opType = OpType.UPDATE)
    public R<Void> updateConfig(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.checkUpdate(sysConfig);
        sysConfigService.updateConfig(sysConfig);
        return saveSuccess();
    }

    /**
     * 删除配置
     */
    @PreAuthorize("@ap.hasAuth('system:config:delete')")
    @PostMapping("/delete")
    @Log(value = "删除配置", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteConfig(@RequestBody Long[] ids) {
        sysConfigService.deleteConfig(ids);
        return deleteSuccess();
    }
}

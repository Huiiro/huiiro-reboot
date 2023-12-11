package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.service.SysDicDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/system/dic/data")
@RequiredArgsConstructor
public class SysDicDataController extends BaseController {

    private final SysDicDataService sysDicDataService;

    /**
     * 获取字典数据分页数据
     */
    @GetMapping("/list")
    public R<Page> getList(SysDicData sysDicData, PageParam pageParam) {
        Page page = sysDicDataService.selectDataList(sysDicData, pageParam);
        return R.ok(page);
    }

    /**
     * 查询单个字典数据
     */
    @GetMapping("/{id}")
    public R<SysDicData> getDicData(@PathVariable Long id) {
        SysDicData sysDicData = sysDicDataService.selectDicDataById(id);
        return R.ok(sysDicData);
    }

    /**
     * 添加字典数据
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加字典数据", opType = OpType.INSERT)
    public R<Void> insertDicData(@Validated @RequestBody SysDicData sysDicData) {
        sysDicDataService.checkInsertDicData(sysDicData);
        sysDicDataService.insertDicData(sysDicData);
        return saveSuccess();
    }

    /**
     * 更新字典数据
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update")
    @Log(value = "更新字典数据", opType = OpType.INSERT)
    public R<Void> updateDicData(@Validated @RequestBody SysDicData sysDicData) {
        sysDicDataService.checkUpdateDicData(sysDicData);
        sysDicDataService.updateDicData(sysDicData);
        return updateSuccess();
    }

    /**
     * 删除字典数据
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete")
    @Log(value = "删除字典数据", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteDicData(@RequestBody Long[] ids) {
        sysDicDataService.deleteDicData(ids);
        return deleteSuccess();
    }
}

package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.service.SysDicTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/system/dic/type")
@RequiredArgsConstructor
public class SysDicTypeController extends BaseController {

    private final SysDicTypeService sysDicTypeService;

    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/refresh")
    public R<Void> refreshCache() {
        sysDicTypeService.refreshCache();
        return R.ok("刷新缓存成功", null);
    }

    /**
     * 获取字典项分页数据
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/list")
    public R<Page> getList(SysDicType sysDicType, PageParam pageParam) {
        Page page = sysDicTypeService.selectTypeList(sysDicType, pageParam);
        return R.ok(page);
    }

    /**
     * 查询单个字典项
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/{id}")
    public R<SysDicType> getDicType(@PathVariable Long id) {
        SysDicType sysDicType = sysDicTypeService.selectDicTypeById(id);
        return R.ok(sysDicType);
    }

    /**
     * 根据名称查询
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/type")
    public R<SysDicType> getDicTypeByType(@RequestParam String dicType) {
        SysDicType sysDicType = sysDicTypeService.selectDicTypeByDicType(dicType);
        return R.ok(sysDicType);
    }

    /**
     * 添加字典项
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加字典项", opType = OpType.INSERT)
    public R<Void> insertDicType(@Validated @RequestBody SysDicType sysDicType) {
        sysDicTypeService.checkInsertDicType(sysDicType);
        sysDicTypeService.insertDicType(sysDicType);
        return saveSuccess();
    }

    /**
     * 更新字典项
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update")
    @Log(value = "更新字典项", opType = OpType.INSERT)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateDicType(@Validated @RequestBody SysDicType sysDicType) {
        sysDicTypeService.checkUpdateDicType(sysDicType);
        sysDicTypeService.updateDicType(sysDicType);
        return updateSuccess();
    }

    /**
     * 删除字典项
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete")
    @Log(value = "删除字典项", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteDicType(@RequestBody Long[] ids) {
        sysDicTypeService.deleteDicType(ids);
        return deleteSuccess();
    }
}

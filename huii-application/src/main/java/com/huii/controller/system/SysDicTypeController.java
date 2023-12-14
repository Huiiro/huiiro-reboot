package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.SysDicType;
import com.huii.common.core.domain.vo.SysDicTypeExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.system.service.SysDicTypeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/system/dic/type")
@RequiredArgsConstructor
public class SysDicTypeController extends BaseController {

    private final SysDicTypeService sysDicTypeService;

    /**
     * 导出字典项
     */
    @PreAuthorize("@ap.hasAuth('system:dic:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出字典项", opType = OpType.EXPORT)
    public void exportDicType(SysDicType sysDicType, HttpServletResponse response) {
        List<SysDicType> list = sysDicTypeService.selectTypeList(sysDicType);
        List<SysDicTypeExportVo> vos = BeanCopyUtils.copyList(list, SysDicTypeExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysDicTypeExportVo.class, response);
    }

    /**
     * 刷新缓存
     */
    @PreAuthorize("@ap.hasAuth('system:dic:edit')")
    @GetMapping("/refresh")
    public R<Void> refreshCache() {
        sysDicTypeService.refreshCache();
        return R.ok("刷新缓存成功", null);
    }

    /**
     * 获取字典项分页数据
     */
    @GetMapping("/list")
    public R<Page> getList(SysDicType sysDicType, PageParam pageParam) {
        Page page = sysDicTypeService.selectTypeList(sysDicType, pageParam);
        return R.ok(page);
    }

    /**
     * 查询单个字典项
     */
    @GetMapping("/{id}")
    public R<SysDicType> getDicType(@PathVariable Long id) {
        SysDicType sysDicType = sysDicTypeService.selectDicTypeById(id);
        return R.ok(sysDicType);
    }

    /**
     * 根据名称查询
     */
    @GetMapping("/type")
    public R<SysDicType> getDicTypeByType(@RequestParam String dicType) {
        SysDicType sysDicType = sysDicTypeService.selectDicTypeByDicType(dicType);
        return R.ok(sysDicType);
    }

    /**
     * 添加字典项
     */
    @PreAuthorize("@ap.hasAuth('system:dic:add')")
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
    @PreAuthorize("@ap.hasAuth('system:dic:edit')")
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
    @PreAuthorize("@ap.hasAuth('system:dic:delete')")
    @PostMapping("/delete")
    @Log(value = "删除字典项", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteDicType(@RequestBody Long[] ids) {
        sysDicTypeService.deleteDicType(ids);
        return deleteSuccess();
    }
}

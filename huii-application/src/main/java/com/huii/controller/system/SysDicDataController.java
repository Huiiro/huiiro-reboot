package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.SysDicData;
import com.huii.common.core.domain.vo.SysDicDataExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.system.service.SysDicDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/system/dic/data")
@RequiredArgsConstructor
public class SysDicDataController extends BaseController {

    private final SysDicDataService sysDicDataService;

    /**
     * 导出字典数据
     */
    @PreAuthorize("@ap.hasAuth('system:dic:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出字典数据", opType = OpType.EXPORT)
    public void exportDicData(SysDicData sysDicData, HttpServletResponse response) {
        List<SysDicData> list = sysDicDataService.selectDataList(sysDicData);
        List<SysDicDataExportVo> vos = BeanCopyUtils.copyList(list, SysDicDataExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysDicDataExportVo.class, response);
    }

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
    @PreAuthorize("@ap.hasAuth('system:dic:add')")
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
    @PreAuthorize("@ap.hasAuth('system:dic:edit')")
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
    @PreAuthorize("@ap.hasAuth('system:dic:delete')")
    @PostMapping("/delete")
    @Log(value = "删除字典数据", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteDicData(@RequestBody Long[] ids) {
        sysDicDataService.deleteDicData(ids);
        return deleteSuccess();
    }
}

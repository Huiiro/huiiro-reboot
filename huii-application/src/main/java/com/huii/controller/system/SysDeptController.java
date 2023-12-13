package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysDept;
import com.huii.common.core.model.R;
import com.huii.common.core.model.Tree;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController extends BaseController {

    private final SysDeptService sysDeptService;

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    public R<List<SysDept>> getList(SysDept sysDept) {
        List<SysDept> list = sysDeptService.selectDeptList(sysDept);
        return R.ok(list);
    }

    /**
     * 获取部门树表
     */
    @GetMapping("/tree")
    public R<List<SysDept>> getTreeList(SysDept sysDept) {
        List<SysDept> dept = sysDeptService.selectDeptList(sysDept);
        List<SysDept> list = sysDeptService.buildTree(dept);
        return R.ok(list);
    }

    /**
     * 获取部门下拉框选项
     */
    @GetMapping("/select")
    public R<List<Tree>> getTreeSelect(SysDept sysDept) {
        List<SysDept> dept = sysDeptService.selectDeptList(sysDept);
        List<SysDept> tree = sysDeptService.buildTree(dept);
        List<Tree> list = sysDeptService.buildSelect(tree, true);
        return R.ok(list);
    }

    /**
     * 构建分配数据权限时的下拉框选项
     */
    @GetMapping("/select/role/{roleId}")
    public R<Map<String, Object>> getTreeSelectByRole(@PathVariable Long roleId) {
        List<SysDept> dept = sysDeptService.selectDeptList(new SysDept());
        List<SysDept> tree = sysDeptService.buildTree(dept);
        Map<String, Object> map = new HashMap<>();
        map.put("tree", sysDeptService.buildSelect(tree, false));
        map.put("keys", sysDeptService.selectDeptIdsByRoleId(roleId));
        return R.ok(map);
    }

    /**
     * 获取部门
     */
    @GetMapping(value = "/{id}")
    public R<SysDept> getDept(@PathVariable Long id) {
        return R.ok(sysDeptService.selectDeptById(id));
    }

    /**
     * 添加部门
     */
    @PreAuthorize("@ap.hasAuth('system:dept:add')")
    @PostMapping("/insert")
    @Log(value = "添加部门", opType = OpType.INSERT)
    public R<Void> insertDept(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.checkInsert(sysDept);
        sysDeptService.insertDept(sysDept);
        return saveSuccess();
    }

    /**
     * 更新部门
     */
    @PreAuthorize("@ap.hasAuth('system:dept:edit')")
    @PostMapping("/update")
    @Log(value = "更新部门", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateDept(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.checkUpdate(sysDept);
        sysDeptService.updateDept(sysDept);
        return updateSuccess();
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ap.hasAuth('system:dept:delete')")
    @PostMapping("/delete/{allow}/{id}")
    @Log(value = "删除部门", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteDept(@PathVariable String allow, @PathVariable Long id) {
        sysDeptService.deleteDept(allow, id);
        return deleteSuccess();
    }
}

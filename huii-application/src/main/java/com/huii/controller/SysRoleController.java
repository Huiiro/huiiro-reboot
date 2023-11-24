package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleService sysRoleService;

    @GetMapping("/list")
    public R<Page> getList(SysRole sysRole, PageParam pageParam) {
        Page page = sysRoleService.selectRoleList(sysRole, pageParam);
        return R.ok(page);
    }

    @GetMapping(value = "/{id}")
    public R<SysRole> getInfo(@PathVariable Long id) {
        return R.ok(sysRoleService.selectRoleById(id));
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加角色", opType = OpType.INSERT)
    public R<SysRole> insertRole(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.checkInsert(sysRole);
        sysRoleService.insertRole(sysRole);
        return saveSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update")
    @Log(value = "更新角色", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRole(@Validated @RequestBody SysRole sysRole) {
        if (SecurityUtils.isRoleAdmin(sysRole.getRoleId())) {
            return R.failed("无法修改管理员信息");
        }
        sysRoleService.checkUpdate(sysRole);
        sysRoleService.updateRole(sysRole);
        return updateSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update/status")
    @Log(value = "更新角色状态", opType = OpType.UPDATE)
    public R<SysRole> updateRoleStatus(@Validated @RequestBody SysRole sysRole) {
        if (SecurityUtils.isRoleAdmin(sysRole.getRoleId())) {
            return R.failed("无法禁用管理员角色");
        }
        sysRoleService.updateRoleStatus(sysRole);
        return updateSuccess();
    }

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update/auths")
    @Log(value = "更新角色权限", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRoleAuths(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.updateRoleAuths(sysRole);
        sysRoleService.clearUserInfoByRoleId(sysRole.getRoleId());
        return updateSuccess();
    }

    //TODO
    //update auth
    //update dataScope ?

    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/delete")
    @Log(value = "删除角色", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> deleteRole(@RequestBody Long[] ids) {
        if (SecurityUtils.isRoleAdmin(ids)) {
            return R.failed("无法删除管理员");
        }
        sysRoleService.deleteRoles(ids);
        return deleteSuccess();
    }
}

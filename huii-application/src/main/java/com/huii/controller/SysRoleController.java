package com.huii.controller;

import com.huii.common.annotation.Log;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.service.SecurityContextService;
import com.huii.system.service.SysRoleService;
import com.huii.system.service.SysUserService;
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
    private final SysUserService sysUserService;
    private final SecurityContextService securityContextService;

    /**
     * 获取角色列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysRole sysRole, PageParam pageParam) {
        Page page = sysRoleService.selectRoleList(sysRole, pageParam);
        return R.ok(page);
    }

    /**
     * 获取角色
     */
    @GetMapping(value = "/{id}")
    public R<SysRole> getRole(@PathVariable Long id) {
        return R.ok(sysRoleService.selectRoleById(id));
    }

    /**
     * 添加角色
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/insert")
    @Log(value = "添加角色", opType = OpType.INSERT)
    public R<SysRole> insertRole(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.checkInsert(sysRole);
        sysRoleService.insertRole(sysRole);
        return saveSuccess();
    }

    /**
     * 更新角色
     */
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

    /**
     * 更新角色状态
     */
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

    /**
     * 更新角色权限
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update/auths")
    @Log(value = "更新角色权限", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRoleAuths(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.updateRoleAuths(sysRole);
        securityContextService.clearUpdateAuthByRoleId(sysRole.getRoleId());
        return updateSuccess();
    }

    /**
     * 更新角色数据权限
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/update/scope")
    @Log(value = "更新角色数据权限", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRoleDataScope(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.updateRoleDataScope(sysRole);
        return updateSuccess();
    }

    /**
     * 删除角色
     */
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

    /**
     * 查询未分配用户
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/query/non")
    public R<Page> queryNonAuthUser(SysUser sysUser, PageParam pageParam) {
        Page page = sysUserService.queryNonAuthUser(sysUser, pageParam);
        return R.ok(page);
    }

    /**
     * 查询已分配用户
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @GetMapping("/query/auth")
    public R<Page> queryAuthUser(SysUser sysUser, PageParam pageParam) {
        Page page =  sysUserService.queryAuthUser(sysUser, pageParam);
        return R.ok(page);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/user/unauth/{roleId}")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> authUser(@PathVariable Long roleId, @RequestBody Long[] userIds) {
        sysUserService.authUser(roleId, userIds);
        securityContextService.clearGrantAuthByRoleId(roleId, userIds);
        return R.ok();
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ap.hasAuth('system:all')")
    @PostMapping("/user/auth/{roleId}")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> unauthUser(@PathVariable Long roleId, @RequestBody Long[] userIds) {
        sysUserService.unauthUser(roleId, userIds);
        securityContextService.clearGrantAuthByRoleId(roleId, userIds);
        return R.ok();
    }
}

package com.huii.controller.system;

import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.domain.vo.SysRoleExportVo;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.enums.ResType;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.common.utils.MessageUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.service.SecurityContextService;
import com.huii.system.service.SysRoleService;
import com.huii.system.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色控制层
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleService sysRoleService;
    private final SysUserService sysUserService;
    private final SecurityContextService securityContextService;

    /**
     * 导出角色
     */
    @PreAuthorize("@ap.hasAuth('system:role:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出角色", opType = OpType.EXPORT)
    public void exportRole(SysRole sysRole, HttpServletResponse response) {
        List<SysRole> list = sysRoleService.selectRoleList(sysRole);
        List<SysRoleExportVo> vos = BeanCopyUtils.copyList(list, SysRoleExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysRoleExportVo.class, response);
    }

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
    @PreAuthorize("@ap.hasAuth('system:role:add')")
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
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
    @PostMapping("/update")
    @Log(value = "更新角色", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRole(@Validated @RequestBody SysRole sysRole) {
        if (SecurityUtils.isRoleAdmin(sysRole.getRoleId())) {
            ResType resType = ResType.SYS_ADMIN_NOT_ALLOW_UPDATE;
            return R.failed(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        sysRoleService.checkUpdate(sysRole);
        sysRoleService.updateRole(sysRole);
        return updateSuccess();
    }

    /**
     * 更新角色状态
     */
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
    @PostMapping("/update/status")
    @Log(value = "更新角色状态", opType = OpType.UPDATE)
    public R<SysRole> updateRoleStatus(@Validated @RequestBody SysRole sysRole) {
        if (SecurityUtils.isRoleAdmin(sysRole.getRoleId())) {
            ResType resType = ResType.SYS_ADMIN_NOT_ALLOW_BAN;
            return R.failed(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        sysRoleService.updateRoleStatus(sysRole);
        return updateSuccess();
    }

    /**
     * 更新角色权限
     */
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
    @PostMapping("/update/auths")
    @Log(value = "更新角色权限", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> updateRoleAuths(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.updateRoleAuths(sysRole);
        Boolean cleanCache = (Boolean) sysRole.getParams().get("cleanCache");
        if (ObjectUtils.isNotEmpty(cleanCache) && cleanCache) {
            securityContextService.clearUpdateAuthByRoleId(sysRole.getRoleId());
        }
        return updateSuccess();
    }

    /**
     * 更新角色数据权限
     */
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
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
    @PreAuthorize("@ap.hasAuth('system:role:delete')")
    @PostMapping("/delete")
    @Log(value = "删除角色", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysRole> deleteRole(@RequestBody Long[] ids) {
        if (SecurityUtils.isRoleAdmin(ids)) {
            ResType resType = ResType.SYS_ADMIN_NOT_ALLOW_DELETE;
            return R.failed(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
        sysRoleService.deleteRoles(ids);
        return deleteSuccess();
    }

    /**
     * 查询未分配用户
     */
    @PreAuthorize("@ap.hasAuth('system:role:query')")
    @GetMapping("/query/non")
    public R<Page> queryNonAuthUser(SysUser sysUser, PageParam pageParam) {
        Page page = sysUserService.queryNonAuthUser(sysUser, pageParam);
        return R.ok(page);
    }

    /**
     * 查询已分配用户
     */
    @PreAuthorize("@ap.hasAuth('system:role:query')")
    @GetMapping("/query/auth")
    public R<Page> queryAuthUser(SysUser sysUser, PageParam pageParam) {
        Page page = sysUserService.queryAuthUser(sysUser, pageParam);
        return R.ok(page);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
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
    @PreAuthorize("@ap.hasAuth('system:role:edit')")
    @PostMapping("/user/auth/{roleId}")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> unauthUser(@PathVariable Long roleId, @RequestBody Long[] userIds) {
        sysUserService.unauthUser(roleId, userIds);
        securityContextService.clearGrantAuthByRoleId(roleId, userIds);
        return R.ok();
    }
}

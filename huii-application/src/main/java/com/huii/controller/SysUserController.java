package com.huii.controller;

import com.huii.auth.service.LoginSecurityService;
import com.huii.common.annotation.Log;
import com.huii.common.annotation.RepeatSubmit;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.domain.vo.SysUserExcelExportVo;
import com.huii.common.core.domain.vo.SysUserExcelImportVo;
import com.huii.common.core.model.Label;
import com.huii.common.core.model.Page;
import com.huii.common.core.model.PageParam;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.excel.ExcelResult;
import com.huii.common.utils.BeanCopyUtils;
import com.huii.common.utils.ExcelUtils;
import com.huii.common.utils.SecurityUtils;
import com.huii.system.listener.SysUserImportListener;
import com.huii.system.service.SysPostService;
import com.huii.system.service.SysRoleService;
import com.huii.system.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;
    private final LoginSecurityService loginSecurityService;

    /**
     * 导出用户
     */
    @PreAuthorize("@ap.hasAuth('system:user:export')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/export")
    @Log(value = "导出用户", opType = OpType.EXPORT)
    public void exportUser(SysUser sysUser, HttpServletResponse response) {
        List<SysUser> list = sysUserService.selectUserList(sysUser);
        List<SysUserExcelExportVo> vos = BeanCopyUtils.copyList(list, SysUserExcelExportVo.class);
        ExcelUtils.exportExcel(null, vos, SysUserExcelExportVo.class, response);
    }

    /**
     * 导出用户模板
     */
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/import/template/down")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtils.exportExcel("用户导入模板", new ArrayList<>(), SysUserExcelImportVo.class, response);
    }

    /**
     * 导入用户
     */
    @PreAuthorize("@ap.hasAuth('system:user:import')")
    @RepeatSubmit(interval = 10000, message = "annotation.repeat.submit.export")
    @RequestMapping("/import{update}")
    @Log(value = "导入用户", opType = OpType.IMPORT)
    public R<Object> importUser(@RequestPart("file") MultipartFile file,
                                @PathVariable(required = false) Boolean update) throws IOException {
        ExcelResult<SysUserExcelImportVo> result = ExcelUtils.importAsyncExcel(
                file.getInputStream(), SysUserExcelImportVo.class,
                new SysUserImportListener(ObjectUtils.isEmpty(update) || update));
        return R.ok(result);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public R<Page> getList(SysUser sysUser, PageParam pageParam) {
        Page page = sysUserService.selectUserList(sysUser, pageParam);
        return R.ok(page);
    }

    /**
     * 获取用户
     */
    @GetMapping(value = {"/", "/{id}"})
    public R<Map<String, Object>> getUser(@PathVariable Long id) {
        List<Label> roleLabels = sysRoleService.selectRolesAll();
        List<Label> postLabels = sysPostService.selectPostsAll();
        SysUser user = sysUserService.selectUserById(id);
        user.setRoleIds(user.getRoles().stream().map(SysRole::getRoleId).toList().toArray(new Long[0]));
        user.setPostIds(sysPostService.selectUserPostIds(id).toArray(new Long[0]));
        Map<String, Object> map = new HashMap<>(3);
        map.put("user", user);
        map.put("roles", roleLabels);
        map.put("posts", postLabels);
        return R.ok(map);
    }

    /**
     * 添加用户
     */
    @PreAuthorize("@ap.hasAuth('system:user:add')")
    @PostMapping("/insert")
    @Log(value = "添加用户", opType = OpType.INSERT)
    public R<SysUser> insertUser(@Validated @RequestBody SysUser sysUser) {
        sysUserService.checkInsert(sysUser);
        sysUserService.insertUser(sysUser);
        return saveSuccess();
    }

    /**
     * 更新用户
     */
    @PreAuthorize("@ap.hasAuth('system:user:edit')")
    @PostMapping("/update")
    @Log(value = "更新用户", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<SysUser> updateUser(@Validated @RequestBody SysUser sysUser) {
        if (SecurityUtils.isAdmin(sysUser.getUserId())) {
            return R.failed("无法修改管理员信息");
        }
        sysUserService.checkUpdate(sysUser);
        sysUserService.updateUser(sysUser);
        return updateSuccess();
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ap.hasAuth('system:user:edit')")
    @PostMapping("/reset/pwd")
    @Log(value = "重置密码", opType = OpType.UPDATE)
    public R<Void> resetPasswordByAdmin(@RequestBody SysUser sysUser) {
        String rawPwd = loginSecurityService.decrypt(sysUser.getEncrypt());
        sysUser.setPassword(SecurityUtils.encryptPassword(rawPwd));
        sysUserService.updateUserPassword(sysUser);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ap.hasAuth('system:user:delete')")
    @PostMapping("/delete")
    @Log(value = "删除用户", opType = OpType.DELETE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> deleteUser(@RequestBody Long[] ids) {
        if (SecurityUtils.isRoleAdmin(ids)) {
            return R.failed("无法删除管理员");
        }
        if (ArrayUtils.contains(ids, getUserId())) {
            return R.failed("无法删除当前用户");
        }
        //TODO check dept role post?
        sysUserService.deleteUsers(ids);
        return deleteSuccess();
    }
}

package com.huii.controller.system;

import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.enums.LocalModules;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysUserProfileController extends BaseController {

    private final SysUserService sysUserService;
    private final LocalService localService;

    /**
     * 获取用户资料
     */
    @GetMapping("/")
    public R<SysUser> loadUserProfile() {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        return R.ok(sysUser);
    }

    /**
     * 更新用户资料
     * 该接口仅更新用户名称，姓名，性别，简介等字段
     */
    @PostMapping("/update")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> updateProfile(@Validated @RequestBody SysUser sysUser) {
        if (!sysUser.getUserId().equals(getUserId())) {
            return R.failed("不允许修改");
        }
        sysUserService.updateUserProfile(sysUser);
        return updateSuccess();
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    @Transactional(rollbackFor = RuntimeException.class)
    public R<String> uploadAvatar(@RequestPart("file") MultipartFile multipartFile) {
        UploadResult uploadResult = localService.uploadFile(multipartFile, LocalModules.AVATAR.getModule());
        String oldAvatar = sysUserService.updateUserAvatar(getUserId(), uploadResult.getUrl());
        try {
            localService.deleteByUrl(oldAvatar);
        } catch (Exception ignored) {}
        return R.ok("上传成功", uploadResult.getUrl());
    }

    //TODO reset password
    //TODO forget password
}

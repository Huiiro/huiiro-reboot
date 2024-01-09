package com.huii.controller.system;

import com.huii.auth.core.entity.ForgetPwdEntity;
import com.huii.auth.core.entity.ResetPwdEntity;
import com.huii.auth.service.LoginSecurityService;
import com.huii.auth.utils.CaptchaGenerator;
import com.huii.common.annotation.Log;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.OpType;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.message.core.service.MailService;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.enums.LocalModules;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Validated
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysUserProfileController extends BaseController {

    private final SysUserService sysUserService;
    private final LoginSecurityService loginSecurityService;
    private final LocalService localService;

    private final RedisTemplateUtils redisTemplateUtils;
    private final MailService mailService;
    private final AliyunSmsServiceImpl aliyunSmsService;

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
        } catch (Exception ignored) {
        }
        return R.ok("上传成功", uploadResult.getUrl());
    }

    /**
     * 修改密码
     */
    @PostMapping("/reset/pwd")
    @Log(value = "修改密码", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> resetPassword(@Validated @RequestBody ResetPwdEntity entity) {
        String oldPwd = loginSecurityService.decrypt(entity.getOldPwd());
        String newPwd = loginSecurityService.decrypt(entity.getPwd());
        sysUserService.updateUserPassword(getUserId(), oldPwd, newPwd);
        return R.ok("密码已修改");
    }

    /**
     * 忘记密码验证
     */
    @GetMapping("/forget/pwd/check")
    @Log(value = "忘记密码", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> forgetPasswordCheck(@RequestParam String type, @RequestParam String identify) {
        String code = CaptchaGenerator.createRandom(6);
        if (type.equals("phone")) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", code);
            aliyunSmsService.send(identify, "toto_your_sms_template", map);
        } else if (type.equals("email")) {
            mailService.sendHtml("【huii】找回密码", "您正在找回密码，您的验证码为"
                    + code + "，验证码有效时间为10分钟！", identify);
        } else {
            return R.failed("验证失败");
        }
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_RESET_PWD_SUFFIX + identify, code,
                CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
        return R.ok("验证码已发送");
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forget/pwd")
    @Log(value = "忘记密码", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> forgetPassword(@Validated @RequestBody ForgetPwdEntity entity) {
        String code = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE_RESET_PWD_SUFFIX + entity.getIdentify());
        if (code == null) {
            return R.failed("验证码已失效");
        }
        if (!code.equals(entity.getCode())) {
            return R.failed("验证码错误");
        }
        String decryptPwd = loginSecurityService.decrypt(entity.getPwd());
        sysUserService.updateUserPassword(entity.getIdentify(), entity.getType(), decryptPwd);
        return R.ok("密码已更新");
    }
}

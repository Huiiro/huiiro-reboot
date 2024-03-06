package com.huii.controller.system;

import com.huii.auth.core.entity.BindEmail;
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
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.message.core.service.MailService;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.oss.core.service.LocalService;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.enums.LocalModules;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
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
     * 获取 忘记密码 验证码
     */
    @GetMapping("/forget/pwd/check")
    @Log(value = "忘记密码", opType = OpType.UPDATE)
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Void> forgetPasswordCheck(@RequestParam String type, @RequestParam String identify) {
        String code = CaptchaGenerator.createRandom(6);
        if (type.equals("phone")) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", code);
            aliyunSmsService.send(identify, "todo_your_sms_template", map);
        } else if (type.equals("email")) {
            mailService.sendHtml("【huii】找回密码", "您正在找回密码，您的验证码为"
                    + code + "，验证码有效时间为10分钟！", identify);
        } else {
            return R.failed("验证方式有误，请重新选择");
        }
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_RESET_PWD_SUFFIX + identify, code,
                CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
        return R.ok("验证码已发送");
    }

    /**
     * 忘记密码/重置密码
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

    /**
     * 查询绑定状态
     */
    @GetMapping("/bind")
    public R<Map<String, String>> getBindInfoStatus() {
        Map<String, String> map = sysUserService.queryUserBindPhoneOrEmail();
        return R.ok(map);
    }

    /**
     * 获取解绑邮箱验证码
     */
    @PostMapping("/email/unbind")
    public R<Void> unbindEmailCode(@RequestBody BindEmail bindEntity) {
        Long userId = getUserId();
        sysUserService.checkUserEmail(bindEntity.getEmail(), userId);
        String code = CaptchaGenerator.createRandom(6);
        mailService.sendHtml("【huii】解绑邮箱", "您正在解绑邮箱，您的验证码为"
                + code + "，验证码有效时间为10分钟！", bindEntity.getEmail());
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_BIND + bindEntity.getEmail(), code,
                CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_BIND_IDENTIFY + userId, userId,
                CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
        return R.ok("验证码已发送");
    }

    /**
     * 校验解绑邮箱验证码
     */
    @PostMapping("/email/unbind/check")
    public R<Boolean> checkUnbindEmailCode(@RequestBody BindEmail bindEntity) {
        checkCode(bindEntity);
        return R.ok(true);
    }

    /**
     * 获取绑定邮箱验证码
     */
    @PostMapping("/email/bind")
    public R<Void> bindEmailCode(@RequestBody BindEmail bindEntity) {
        String code = CaptchaGenerator.createRandom(6);
        mailService.sendHtml("【huii】绑定邮箱", "您正在绑定邮箱，您的验证码为"
                + code + "，验证码有效时间为10分钟！", bindEntity.getEmail());
        redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_BIND + bindEntity.getEmail(), code,
                CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
        return R.ok("验证码已发送");
    }

    /**
     * 校验绑定邮箱验证码
     */
    @PostMapping("/email/bind/check")
    public R<Void> bindEmail(@RequestBody BindEmail bindEntity) {
        checkCode(bindEntity);
        //获取是否需要验证提前解绑
        Map<String, String> map = sysUserService.queryUserBindPhoneOrEmail();
        //已绑定，需要校验是否已经解绑旧邮箱
        if (map.get("emailStatus").equals("1")) {
            Object object = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE_BIND_IDENTIFY + getUserId());
            if (ObjectUtils.isEmpty(object)) {
                throw new ServiceException("非法的操作，请尝试重新绑定");
            }
        }
        sysUserService.bindEmail(getUserId(), bindEntity.getEmail());
        redisTemplateUtils.deleteObject(CacheConstants.VERIFY_CODE_BIND_IDENTIFY + getUserId());
        return R.ok("绑定成功");
    }

    private void checkCode(BindEmail bindEntity) {
        String code = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE_BIND + bindEntity.getEmail());
        if (StringUtils.isBlank(code)) {
            throw new ServiceException("验证码已过期");
        }
        if (!StringUtils.isBlank(bindEntity.getCode()) && !code.equals(bindEntity.getCode())) {
            throw new ServiceException("验证码错误");
        }
        redisTemplateUtils.deleteObject(CacheConstants.VERIFY_CODE_BIND + bindEntity.getEmail());
    }
}

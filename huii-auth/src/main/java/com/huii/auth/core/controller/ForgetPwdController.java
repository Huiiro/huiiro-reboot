package com.huii.auth.core.controller;

import com.huii.auth.core.entity.ForgetPwdEntity;
import com.huii.auth.service.ForgetPwdService;
import com.huii.auth.service.LoginSecurityService;
import com.huii.auth.service.LoginService;
import com.huii.auth.utils.CaptchaGenerator;
import com.huii.common.annotation.Anonymous;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.R;
import com.huii.common.utils.redis.RedisTemplateUtils;
import com.huii.message.core.service.MailService;
import com.huii.message.core.service.impl.AliyunSmsServiceImpl;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 忘记密码接口/未登录接口
 *
 * @author huii
 */
@RestController
@RequiredArgsConstructor
public class ForgetPwdController {

    private final LoginService loginService;
    private final LoginSecurityService loginSecurityService;
    private final ForgetPwdService forgetPwdService;
    private final MailService mailService;
    private final AliyunSmsServiceImpl aliyunSmsService;
    private final RedisTemplateUtils redisTemplateUtils;
    private final SysUserService sysUserService;

    @Anonymous
    @GetMapping("/forget/pwd/code")
    public R<Void> getForgetPwdCode(@RequestParam String type, @RequestParam String identify) {
        try {
            String username = loginService.getUsername(identify);
            if (!"phone".equals(type) && !"email".equals(type)) {
                return R.failed("验证方式有误，请重新选择");
            }
            SysUser user = forgetPwdService.getUser(username);
            if (null == user) {
                return R.failed("您找回的账号不存在");
            }
            String code = CaptchaGenerator.createRandom(6);
            if (type.equals("phone")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", code);
                aliyunSmsService.send(identify, "todo_your_sms_template", map);
            } else {
                mailService.sendHtml("【huii】找回密码", "您正在找回密码，您的验证码为"
                        + code + "，验证码有效时间为10分钟！", identify);
            }
            redisTemplateUtils.setCacheObject(CacheConstants.VERIFY_CODE_RESET_PWD_SUFFIX + identify, code,
                    CacheConstants.DEFAULT_CACHE_TIME, TimeUnit.MINUTES);
            return R.ok("验证码已发送");
        } catch (Exception e) {
            return R.failed("遇到问题了，请稍后尝试！");
        }
    }

    @Anonymous
    @PostMapping("/forget/pwd/check")
    public R<Void> checkForgetPwdCode(@RequestBody ForgetPwdEntity entity) {
        String code = redisTemplateUtils.getCacheObject(CacheConstants.VERIFY_CODE_RESET_PWD_SUFFIX + entity.getIdentify());
        if (code == null) {
            return R.failed("验证码已失效");
        }
        if (!code.equals(entity.getCode())) {
            return R.failed("验证码错误");
        }
        sysUserService.updateUserPassword(entity.getIdentify(), entity.getType(), loginSecurityService.decrypt(entity.getPwd()));
        return R.ok("密码已修改！");
    }
}

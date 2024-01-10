package com.huii.auth.core.controller;

import com.huii.auth.core.entity.RegisterEntity;
import com.huii.auth.service.RegisterService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.enums.ResType;
import com.huii.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 注册接口
 *
 * @author huii
 */
@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    /**
     * 校验用户名是否重复
     */
    @Anonymous
    @GetMapping("/register/check/username")
    public R<Object> checkUsername(@RequestParam String username) {
        return R.ok(registerService.checkUsername(username));
    }

    /**
     * 注册接口
     */
    @Anonymous
    @PostMapping("/register")
    public R<Object> register(@Validated @RequestBody RegisterEntity entity) {
        registerService.register(entity);
        return R.ok(MessageUtils.message(ResType.USER_REGISTER_SUCCESS.getI18n()));
    }
}

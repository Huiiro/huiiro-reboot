package com.huii.auth.core.controller;

import com.huii.auth.core.service.LoginSecurityService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.annotation.Xss;
import com.huii.common.core.model.R;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录安全接口
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/auth/v1/security")
@RequiredArgsConstructor
public class LoginSecurityController {

    private final LoginSecurityService loginSecurityService;

    /**
     * 获取公钥
     */
    @Anonymous
    @GetMapping("/key")
    public R<Map<String, Object>> key() {
        String publicKey = loginSecurityService.getPublicKeyPem();
        Map<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        return R.ok(map);
    }

    /**
     * 刷新token
     */
    @Anonymous
    @GetMapping("/refresh")
    public R<Map<String, Object>> getNewAccessToken(@NotBlank @RequestParam String refresh) {
        String token = loginSecurityService.createNewAccessToken(refresh);
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", token);
        return token == null ? R.failed() : R.ok(map);
    }

    /**
     * 加密接口
     */
    @Anonymous
    @GetMapping("/encrypt")
    public R<Object> encrypt(@RequestParam @Xss String str) {
        String text = loginSecurityService.encrypt(str);
        Map<String, Object> map = new HashMap<>();
        map.put("encryptText", text);
        return R.ok(map);
    }

    /**
     * 解密接口
     */
    @Anonymous
    @GetMapping("/decrypt")
    public R<Object> decrypt(@RequestParam @Xss String str) {
        String text = loginSecurityService.decrypt(str.replaceAll(" ", "+"));
        Map<String, Object> map = new HashMap<>();
        map.put("decryptText", text);
        return R.ok(map);
    }
}

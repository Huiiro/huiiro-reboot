package com.huii.auth.core.controller;

import com.huii.auth.service.LoginSecurityService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/auth/v1/security")
@RequiredArgsConstructor
public class LoginSecurityController {

    private final LoginSecurityService loginSecurityService;

    @Anonymous
    @GetMapping("/key")
    public R<Object> key() {
        String publicKey = loginSecurityService.getPublicKeyPem();
        Map<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        return R.ok(map);
    }

    @Anonymous
    @GetMapping("/test/encrypt")
    public R<Object> encrypt(@RequestParam String str) {
        String text = loginSecurityService.encrypt(str);
        Map<String, Object> map = new HashMap<>();
        map.put("encryptText", text);
        return R.ok(map);
    }

    @Anonymous
    @GetMapping("/test/decrypt")
    public R<Object> decrypt(@RequestParam String str) {
        String text = loginSecurityService.decrypt(str.replaceAll(" ", "+"));
        Map<String, Object> map = new HashMap<>();
        map.put("decryptText", text);
        return R.ok(map);
    }
}

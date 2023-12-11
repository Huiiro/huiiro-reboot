package com.huii.auth.core.controller;

import com.huii.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Anonymous
    @GetMapping("/")
    public String index() {
        return "欢迎访问huii系统";
    }
}

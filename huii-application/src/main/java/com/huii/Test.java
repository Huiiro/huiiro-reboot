package com.huii;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.domain.SysUser;
import com.huii.common.core.model.R;
import com.huii.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {

    private final SysUserService service;

    //@PreAuthorize("hasAnyAuthority()")
    @Anonymous
    @GetMapping("/1")
    public R<SysUser> test1() {
        return R.ok();
    }

    @Anonymous
    @GetMapping("/2/{id}")
    public String test2(@PathVariable String id) {
        return id;
    }

    @PreAuthorize("@ap.hasAuth('test')")
    @GetMapping("/3")
    public String test3() {
        return "need auth!";
    }
}

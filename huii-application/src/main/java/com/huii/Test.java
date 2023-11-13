package com.huii;

import com.huii.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class Test {

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping("/1")
    public String test1() {
        return "1";
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

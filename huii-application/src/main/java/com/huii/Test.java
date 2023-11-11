package com.huii;

import com.huii.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.*;

@Anonymous
@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping("/1")
    public String test1() {
        return "1";
    }

    @GetMapping("/2/{id}")
    public String test2(@PathVariable String id) {
        return id;
    }
}

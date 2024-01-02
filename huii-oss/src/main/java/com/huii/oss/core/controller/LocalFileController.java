package com.huii.oss.core.controller;

import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Anonymous
@RestController
@RequestMapping("/oss/local/file")
@RequiredArgsConstructor
public class LocalFileController extends BaseController {
}

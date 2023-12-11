package com.huii.controller;

import com.huii.common.core.model.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/system/cache")
@RequiredArgsConstructor
public class SysCacheController extends BaseController {
}

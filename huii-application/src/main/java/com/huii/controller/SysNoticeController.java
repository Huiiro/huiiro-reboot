package com.huii.controller;

import com.huii.common.core.model.base.BaseController;
import com.huii.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class SysNoticeController extends BaseController {

    private final SysNoticeService sysNoticeService;
}

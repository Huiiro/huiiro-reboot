package com.huii.controller.system;

import com.huii.common.core.model.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统OSS控制层
 *
 * @author huii
 */
@Validated
@RestController
@RequestMapping("/system/oss")
@RequiredArgsConstructor
public class SysOssController extends BaseController {
}

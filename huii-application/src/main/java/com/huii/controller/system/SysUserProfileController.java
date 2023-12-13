package com.huii.controller.system;

import com.huii.common.core.model.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysUserProfileController extends BaseController {
}

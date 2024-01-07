package com.huii.auth.core.controller;

import com.huii.auth.core.entity.dto.Oauth2Dto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.base.BaseController;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * oauth2 登录回调地址
 *
 * @author huii
 */
@Validated
@Anonymous
@RestController
@RequestMapping("/callback")
@RequiredArgsConstructor
public class Oauth2CallbackController extends BaseController {

    private final LoginService loginService;

    /**
     * github
     */
    @RequestMapping("/github")
    public void github(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam HashMap<String, Object> map) {
        toggleLogin(LoginType.GITHUB, map, request, response);
    }

    /**
     * gitee
     */
    @RequestMapping("/gitee")
    public void gitee(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam HashMap<String, Object> map) {
        toggleLogin(LoginType.GITEE, map, request, response);
    }

    private void toggleLogin(LoginType loginType, HashMap<String, Object> map,
                             HttpServletRequest request, HttpServletResponse response) {
        Oauth2Dto dto = new Oauth2Dto();
        dto.setLoginType(loginType);
        dto.setCode((String) map.get("code"));
        dto.setState((String) map.get("state"));
        long state = Long.parseLong((String) map.get("state"));
        if (state != 0) {
            dto.setHasLoginAndDoBind(SystemConstants.STATUS_1);
            dto.setBindId(state);
        }
        LoginVo loginVo = loginService.oauth2Login(dto, request);
        loginService.defaultOauth2LoginResponse(loginVo, response);
    }
}

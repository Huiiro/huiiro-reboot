package com.huii.auth.core.controller;

import com.huii.auth.core.entity.dto.Oauth2Dto;
import com.huii.auth.core.entity.vo.LoginVo;
import com.huii.auth.service.LoginService;
import com.huii.common.annotation.Anonymous;
import com.huii.common.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Validated
@Anonymous
@RestController
@RequestMapping("/callback")
@RequiredArgsConstructor
public class LoginOauth2Controller {

    private final LoginService loginService;

    /**
     * github
     */
    @RequestMapping("/github")
    public void github(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam HashMap<String, Object> map) {
        Oauth2Dto dto = new Oauth2Dto();
        dto.setLoginType(LoginType.GITHUB);
        dto.setCode((String) map.get("code"));
        dto.setState((String) map.get("state"));
        LoginVo loginVo = loginService.oauth2Login(dto, request);
        loginService.defaultOauth2LoginResponse(loginVo, response);
    }

    /**
     * gitee
     */
    @RequestMapping("/gitee")
    public void gitee(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam HashMap<String, Object> map) {
        Oauth2Dto dto = new Oauth2Dto();
        dto.setLoginType(LoginType.GITEE);
        dto.setCode((String) map.get("code"));
        dto.setState((String) map.get("state"));
        LoginVo loginVo = loginService.oauth2Login(dto, request);
        loginService.defaultOauth2LoginResponse(loginVo, response);
    }
}

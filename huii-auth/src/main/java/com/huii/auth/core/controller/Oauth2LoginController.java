package com.huii.auth.core.controller;

import com.huii.auth.config.properties.LoginProperties;
import com.huii.common.annotation.Anonymous;
import com.huii.common.core.model.R;
import com.huii.common.core.model.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 0auth2 登录请求地址
 *
 * @author huii
 */
@Validated
@Anonymous
@RestController
@RequestMapping("/oauth/login")
@RequiredArgsConstructor
public class Oauth2LoginController extends BaseController {

    private final LoginProperties loginProperties;

    /**
     * github
     */
    @RequestMapping("/github/{origin}")
    public R<Map<String, String>> github(@PathVariable String origin) {
        String url = "https://github.com/login/oauth/authorize?" +
                "client_id=c673ed05ca8d98032a1e" +
                "&redirect_uri=" + loginProperties.getDefaultOauth2LoginRedirectUrl() + "/github" +
                "&state=" + origin + getUserIdAsState() +
                "&scope=user";
        return buildResult(url);
    }

    /**
     * gitee
     */
    @RequestMapping("/gitee/{origin}")
    public R<Map<String, String>> gitee(@PathVariable String origin) {
        String url = "https://gitee.com/oauth/authorize?" +
                "client_id=a69c6d6902bb792281a5897ba02d8a5ee2cb7e8f8a6b3a5e5cb1b6a809893ee5" +
                "&redirect_uri=" + loginProperties.getDefaultOauth2LoginRedirectUrl() + "/gitee" +
                "&state=" + origin + getUserIdAsState() +
                "&response_type=code" +
                "&scope=user_info";
        return buildResult(url);
    }

    /**
     * 获取用户ID
     * 如果能获取到，表明用户已登录，那么则进行绑定操作
     * 如果不能获取到，则表明用户未登录，那么进行登陆操作
     */
    private String getUserIdAsState() {
        Long userId = safeGetUserId();
        String state = "0";
        if (userId != null) {
            state = userId.toString();
        }
        return state;
    }

    /**
     * 构建返回结果，由于设置重定向不生效，采用手动跳转解决
     * <p>response.setStatus(HttpServletResponse.SC_FOUND);</p>
     * <p>response.setHeader("Location", url);</p>
     */
    private R<Map<String, String>> buildResult(String url) {
        Map<String, String> map = new HashMap<>(1);
        map.put("url", url);
        return R.ok(map);
    }
}

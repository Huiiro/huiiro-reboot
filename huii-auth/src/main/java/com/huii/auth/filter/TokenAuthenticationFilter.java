package com.huii.auth.filter;

import com.alibaba.fastjson2.JSONObject;
import com.huii.auth.config.properties.JwtProperties;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.constants.SystemConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.redis.RedisTemplateUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token filter
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final RedisTemplateUtils redisTemplateUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            //token为空 直接放行
            filterChain.doFilter(request, response);
            return;
        }
        if (StringUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            Long id = jwtUtils.getId(token);
            if (id == null) {
                request.setAttribute("tokenException", "e");
            } else {
                //单token情况是否验证redis中的token
                if (SystemConstants.FALSE.equals(jwtProperties.getEnableDoubleToken()) &&
                        SystemConstants.TRUE.equals(jwtProperties.getSaveToken())) {
                    String redisToken = redisTemplateUtils.getCacheObject(CacheConstants.TOKEN + id);
                    if (ObjectUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                //双token情况是否验证redis中的token
                if (SystemConstants.TRUE.equals(jwtProperties.getEnableDoubleToken()) &&
                        SystemConstants.TRUE.equals(jwtProperties.getSaveAccessToken())) {
                    String redisToken = redisTemplateUtils.getCacheObject(CacheConstants.TOKEN + id);
                    if (ObjectUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                //获取用户信息
                JSONObject object = redisTemplateUtils.getCacheObject(CacheConstants.USER + id);
                LoginUser loginUser = object.toJavaObject(LoginUser.class);
                if (ObjectUtils.isNotEmpty(loginUser)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

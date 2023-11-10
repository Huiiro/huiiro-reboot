package com.huii.auth.filter;

import com.huii.auth.config.properties.JwtProperties;
import com.huii.auth.utils.JwtUtils;
import com.huii.common.constants.CacheConstants;
import com.huii.common.core.model.LoginUser;
import com.huii.common.utils.redis.RedisTemplateUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final RedisTemplateUtils redisTemplateUtils;

    @SuppressWarnings("all")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (StringUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            Long id = jwtUtils.getId(token);
            if (id != null) {
                //单token情况是否验证redis中的token
                if (jwtProperties.getEnableDev().equals("false") &&
                        jwtProperties.getEnableDoubleToken().equals("false") &&
                        jwtProperties.getSaveToken().equals("true")) {
                    String redisToken = redisTemplateUtils.getCacheObject(CacheConstants.TOKEN + id);
                    if (ObjectUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                //双token情况是否验证redis中的token
                if (jwtProperties.getEnableDoubleToken().equals("true") &&
                        jwtProperties.getSaveAccessToken().equals("true")) {
                    String redisToken = redisTemplateUtils.getCacheObject(CacheConstants.TOKEN + id);
                    if (ObjectUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                //获取用户信息
                LoginUser loginUser = redisTemplateUtils.getCacheObject(CacheConstants.USER + id);
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

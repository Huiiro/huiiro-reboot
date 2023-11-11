package com.huii.common.filter;

import com.huii.common.filter.wrapper.XssHttpServletRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 防止XSS攻击的过滤器
 *
 * @author huii
 */
public class XssFilter implements Filter {

    public List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            excludes.addAll(Arrays.asList(url));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleUrls(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleUrls(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getServletPath();
        String method = request.getMethod();
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            return true;
        }
        return matches(url, excludes);
    }

    @Override
    public void destroy() {

    }

    public static boolean matches(String str, List<String> strList) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty((CharSequence) strList)) {
            return false;
        }
        for (String pattern : strList) {
            AntPathMatcher matcher = new AntPathMatcher();
            return matcher.match(pattern, str);
        }
        return false;
    }
}
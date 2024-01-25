package com.huii.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.io.IOException;
import java.util.Map;

/**
 * resource filter
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class ResourceFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping mapping;
    private final PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        if (requestMethod.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
        }
        String requestedResource = request.getRequestURI();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        RequestMappingInfo matchingMappingInfo = handlerMethods.keySet().stream()
                .filter(mappingInfo -> {
                    assert mappingInfo.getPathPatternsCondition() != null;
                    return pathMatcher.match(mappingInfo.getPathPatternsCondition().getPatterns()
                            .stream().findFirst().map(PathPattern::getPatternString)
                            .orElse(""), requestedResource);
                }).findFirst().orElse(null);
        if (matchingMappingInfo == null) {
            response.setStatus(404);
        }
        filterChain.doFilter(request, response);
    }
}
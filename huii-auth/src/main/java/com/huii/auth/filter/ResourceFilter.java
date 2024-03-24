package com.huii.auth.filter;

import com.huii.auth.config.properties.SecurityProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * resource filter
 *
 * @author huii
 */
@Component
public class ResourceFilter extends OncePerRequestFilter {

    @Resource
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping mapping;
    @Resource
    private PathMatcher pathMatcher;
    private static Set<String> patternStrings;
    @Resource
    private SecurityProperties securityProperties;

    @PostConstruct
    public void init() {
        patternStrings = mapping.getHandlerMethods().keySet().stream().map(handlerMethod -> {
            if (handlerMethod.getPathPatternsCondition() != null) {
                return handlerMethod.getPathPatternsCondition().getPatterns();
            } else {
                return Collections.<PathPattern>emptySet();
            }
        }).flatMap(Collection::stream).map(PathPattern::getPatternString).collect(Collectors.toSet());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        if (requestMethod.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
        }

        String requestedResource = request.getRequestURI();
        boolean allowFlag = false;
        for (String allow : securityProperties.getAllows()) {
            if (pathMatcher.match(allow, requestedResource)) {
                allowFlag = true;
                break;
            }
        }
        if (allowFlag) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isMatched = patternStrings.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestedResource));
        if (!isMatched) {
            response.setStatus(404);
        }

        filterChain.doFilter(request, response);
    }
}
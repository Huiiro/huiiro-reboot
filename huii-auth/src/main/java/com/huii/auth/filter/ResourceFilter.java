package com.huii.auth.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ResourceFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping mapping;
    private final PathMatcher pathMatcher;
    private static Set<String> patternStrings;

    @PostConstruct
    public void init() {
        patternStrings = mapping.getHandlerMethods().keySet().stream()
                .map(handlerMethod -> {
                    if (handlerMethod.getPathPatternsCondition() != null) {
                        return handlerMethod.getPathPatternsCondition().getPatterns();
                    } else {
                        return Collections.<PathPattern>emptySet();
                    }
                })
                .flatMap(Collection::stream)
                .map(PathPattern::getPatternString)
                .collect(Collectors.toSet());

    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        if (requestMethod.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
        }
        String requestedResource = request.getRequestURI();
        boolean isMatched = patternStrings.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestedResource));
        if (!isMatched) {
            response.setStatus(404);
        }
        filterChain.doFilter(request, response);
    }
}
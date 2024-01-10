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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

/**
 * resource filter
 *
 * @author huii
 */
@Component
@RequiredArgsConstructor
public class ResourceExistenceFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping mapping;
    private final PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        if (requestMethod.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
        }

//        String requestedResource = request.getRequestURI();
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
//        RequestMappingInfo matchingMappingInfo = handlerMethods.keySet().stream()
//                .filter(mappingInfo -> {
//                    assert mappingInfo.getPathPatternsCondition() != null;
//                    return pathMatcher.match(mappingInfo.getPathPatternsCondition().getPatterns()
//                            .stream().findFirst().map(PathPattern::getPatternString)
//                            .orElse(""), requestedResource);
//                })
//                .findFirst()
//                .orElse(null);
//        if (matchingMappingInfo == null) {
//            //404
//            ResType type = ResType.STATUS_NOT_FOUND;
//            R<Object> result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
//            JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_NOT_FOUND, result);
//            return;
//        }
//
//        Optional<RequestMethod> optional = matchingMappingInfo.getMethodsCondition().getMethods().stream().findFirst();
//        if (optional.isPresent()) {
//            RequestMethod method = optional.get();
//            if (!method.toString().equals(requestMethod)) {
//                //405
//                ResType type = ResType.STATUS_METHOD_NOT_ALLOWED;
//                R<Object> result = R.failed(type.getCode(), ResType.getI18nMessage(type), null);
//                JsonWriteUtils.writeOptJson(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, result);
//                return;
//            }
//        }

        filterChain.doFilter(request, response);
    }
}

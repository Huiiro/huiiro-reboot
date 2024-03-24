package com.huii.auth.config.properties;

import com.huii.common.annotation.Anonymous;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 安全参数配置
 * 白名单路径配置
 * config.security
 *
 * @author huii
 */
@SuppressWarnings("all")
@Configuration
@RequiredArgsConstructor
public class SecurityProperties implements InitializingBean, ApplicationContextAware {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    private String rememberMeKey = "rememberMeKey";
    private final Set<String> allows = new HashSet<>();
    private final Set<String> forbiddens = new HashSet<>();
    private ApplicationContext applicationContext;
    private final Environment env;
    @Resource
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping mapping;

    @PostConstruct
    public void init() {
        String[] allowsArray = env.getProperty("security.allows", String[].class);
        if (allowsArray != null) {
            allows.addAll(Arrays.asList(allowsArray));
        }
        String[] forbiddensArray = env.getProperty("security.forbiddens", String[].class);
        if (forbiddensArray != null) {
            forbiddens.addAll(Arrays.asList(forbiddensArray));
        }
        String rememberMe = env.getProperty("security.remember", String.class);
        if (StringUtils.isNoneEmpty(rememberMe)) {
            rememberMeKey = rememberMe;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        //RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(i -> {
            HandlerMethod handlerMethod = map.get(i);
            Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
            if (method != null && i.getPathPatternsCondition() != null) {
                i.getPathPatternsCondition().getPatterns().forEach(url -> allows.add(RegExUtils.replaceAll(url.getPatternString(), PATTERN, "**")));
            }
            Anonymous controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
            if (controller != null && i.getPathPatternsCondition() != null) {
                RequestMapping requestMap = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequestMapping.class);
                if (requestMap != null) {
                    Arrays.stream(requestMap.path()).forEach(req -> allows.add(processSecurityPath(req)));
                }
            }
        });
    }

    public String[] getAllows() {
        return allows.toArray(new String[0]);
    }

    public String[] getForbiddens() {
        return forbiddens.toArray(new String[0]);
    }

    private static String processSecurityPath(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1) + "/**";
        } else {
            return path + "/**";
        }
    }

    public String getRememberMeKey() {
        return rememberMeKey;
    }
}

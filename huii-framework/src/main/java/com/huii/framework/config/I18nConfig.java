package com.huii.framework.config;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * 国际化配置
 * <p>前端配置</p>
 * <p>config.headers['Accept-Language'] = ‘en_US’;</p>
 *
 * @author huii
 */
@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

    @SuppressWarnings("all")
    static class I18nLocaleResolver implements LocaleResolver {

        @SneakyThrows
        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String language = request.getHeader("Accept-Language");
            Locale locale = Locale.getDefault();
            if (StrUtil.isNotBlank(language)) {
                String[] split;
                if (language.contains("_")) {
                    split = language.split("_");
                } else {
                    split = language.split("-");
                }
                locale = new Locale(split[0], split[1]);
            }
            return locale;
        }

        @Override
        public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        }
    }
}

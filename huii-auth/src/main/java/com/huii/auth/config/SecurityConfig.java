package com.huii.auth.config;

import com.huii.auth.config.properties.SecurityProperties;
import com.huii.auth.filter.ResourceExistenceFilter;
import com.huii.auth.filter.TokenAuthenticationFilter;
import com.huii.auth.handler.AuthenticationAccessDeniedHandler;
import com.huii.auth.handler.AuthenticationEntryPointHandler;
import com.huii.auth.handler.LogoutSuccessHandler;
import com.huii.auth.provider.AccountAuthenticationProvider;
import com.huii.auth.provider.EmailAuthenticationProvider;
import com.huii.auth.provider.Oauth2AuthenticationProvider;
import com.huii.auth.provider.SmsAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * security安全配置
 *
 * @author huii
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;
    private final AccountAuthenticationProvider accountAuthenticationProvider;
    private final SmsAuthenticationProvider smsAuthenticationProvider;
    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final Oauth2AuthenticationProvider oauth2AuthenticationProvider;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final ResourceExistenceFilter resourceExistenceFilter;

    /**
     * security配置链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .headers((h) -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .rememberMe((r -> r.rememberMeServices(tokenBasedRememberMeServices())))
                .formLogin(FormLoginConfigurer::disable)
                .logout((l) -> l.logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler))
                .authorizeHttpRequests((a) -> a.requestMatchers(securityProperties.getAllows()).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPointHandler)
                        .accessDeniedHandler(authenticationAccessDeniedHandler))
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(resourceExistenceFilter, TokenAuthenticationFilter.class);
        return http.build();
    }

    /**
     * passwordEncoder BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * rememberMeService
     */
    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices =
                new TokenBasedRememberMeServices(securityProperties.getRememberMeKey(), userDetailsService);
        rememberMeServices.setTokenValiditySeconds(86400);
        rememberMeServices.setCookieName("remember");
        rememberMeServices.setParameter("rememberMe");
        return rememberMeServices;
    }

    /**
     * 使用自定义AuthenticationProvider时，DaoAuthenticationProvider不会被注入
     * 需要手动创建bean，注入到AuthenticationManager中
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    /**
     * 全局AuthenticationManager对象获取
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(accountAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(smsAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(emailAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(oauth2AuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
}

package com.slender.config.configuration;

import com.slender.config.manager.AuthenticationTokenManager;
import com.slender.config.manager.FilterConfigManager;
import com.slender.config.manager.RequestConfigManager;
import com.slender.constant.user.UserConstant;
import com.slender.filter.*;
import com.slender.handler.*;
import com.slender.provider.CaptchaProvider;
import com.slender.provider.PasswordProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    public interface SecurityURLManager {
        String LOGIN_PASSWORD = "/auth/login/password";
        String LOGIN_CAPTCHA = "/auth/login/captcha";
        String CAPTCHA ="/auth/captcha";
        String REGISTER = "/auth/register";
        String RESET = "/auth/reset";
        String LOGOUT = "/auth/logout";
        String REFRESH = "/auth/refresh";
        String API = "/api-docs";
    }

    @Bean
    public FilterConfigManager filterConfigManager(){
        return new FilterConfigManager()
                .addFilterConfig(MultiPasswordFilter.class, SecurityURLManager.LOGIN_PASSWORD)
                .addFilterConfig(CaptchaFilter.class, SecurityURLManager.LOGIN_CAPTCHA);
    }

    @Bean
    public RequestConfigManager requestMatcher(){
        return new RequestConfigManager()
                .addPOSTURIConfig(SecurityURLManager.LOGIN_PASSWORD, false)
                .addPOSTURIConfig(SecurityURLManager.CAPTCHA, false)
                .addPOSTURIConfig(SecurityURLManager.LOGIN_CAPTCHA, false)
                .addPOSTURIConfig(SecurityURLManager.REGISTER, false)
                .addPOSTURIConfig(SecurityURLManager.RESET, true)
                .addGETURIConfig(SecurityURLManager.REFRESH,true)
                .addGETURIConfig(SecurityURLManager.API,false)
                .addDELETEURIConfig(SecurityURLManager.LOGOUT,true);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SecurityExceptionHandler securityExceptionHandler,
                                           SignOutHandler signOutHandler,
                                           SignOutSuccessHandler signOutSuccessHandler,
                                           AccessRefuseHandler accessRefuseHandler,
                                           MultiPasswordFilter multiPasswordFilter,
                                           RequestTypeFilter requestTypeFilter,
                                           JwtFilter jwtFilter,
                                           CaptchaFilter captchaFilter
    )throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout
                        .logoutUrl(SecurityURLManager.LOGOUT)
                        .addLogoutHandler(signOutHandler)
                        .logoutSuccessHandler(signOutSuccessHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/merchants/**").hasAnyAuthority(UserConstant.Authority.MERCHANT, UserConstant.Authority.ADMINISTRATION)
                        .requestMatchers("/admins/**").hasAuthority(UserConstant.Authority.ADMINISTRATION)
                        .anyRequest().authenticated()
                )
                .addFilterAt(multiPasswordFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter, MultiPasswordFilter.class)
                .addFilterBefore(jwtFilter, LogoutFilter.class)
                .addFilterBefore(requestTypeFilter,JwtFilter.class)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(accessRefuseHandler))
                .build();
    }

    @Bean
    public MultiPasswordFilter passwordFilter(FilterConfigManager filterConfigManager,
                                              AuthenticationManager authenticationManager,
                                              AuthSuccessHandler authSuccessHandler,
                                              AuthFailureHandler authFailureHandler
    ){
        MultiPasswordFilter multiPasswordFilter = new MultiPasswordFilter(filterConfigManager);
        multiPasswordFilter.setAuthenticationManager(authenticationManager);
        multiPasswordFilter.setAuthenticationFailureHandler(authFailureHandler);
        multiPasswordFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return multiPasswordFilter;
    }

    @Bean
    public CaptchaFilter captchaFilter(FilterConfigManager filterConfigManager,
                                       AuthSuccessHandler authSuccessHandler,
                                       AuthenticationManager authenticationManager,
                                       AuthFailureHandler authFailureHandler
    ){
        CaptchaFilter captchaFilter = new CaptchaFilter(filterConfigManager);
        captchaFilter.setAuthenticationManager(authenticationManager);
        captchaFilter.setAuthenticationFailureHandler(authFailureHandler);
        captchaFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return captchaFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CaptchaProvider captchaProvider,
                                                       PasswordProvider passwordProvider){
        AuthenticationTokenManager authenticationTokenManager=new AuthenticationTokenManager();
        return authenticationTokenManager.addProviders(captchaProvider)
                .addProviders(passwordProvider);
    }

}


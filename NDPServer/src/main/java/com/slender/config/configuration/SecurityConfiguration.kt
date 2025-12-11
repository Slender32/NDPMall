package com.slender.config.configuration

import com.slender.config.manager.AuthenticationTokenManager
import com.slender.config.manager.FilterConfigManager
import com.slender.config.manager.RequestConfigManager
import com.slender.constant.user.UserConstant
import com.slender.filter.CaptchaFilter
import com.slender.filter.JwtFilter
import com.slender.filter.MultiPasswordFilter
import com.slender.filter.RequestTypeFilter
import com.slender.handler.*
import com.slender.provider.CaptchaProvider
import com.slender.provider.PasswordProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
@EnableMethodSecurity
open class SecurityConfiguration {

    companion object SecurityURLManager {
        const val LOGIN_PASSWORD = "/auth/login/password"
        const val LOGIN_CAPTCHA = "/auth/login/captcha"
        const val CAPTCHA = "/auth/captcha"
        const val REGISTER = "/auth/register"
        const val LOGOUT = "/users/*/logout"
        const val REFRESH="/users/*/refresh"
        const val API = "/api-docs"
    }

    @Bean
    open fun filterConfigManager(): FilterConfigManager {
        return FilterConfigManager()
            .addFilterConfig(MultiPasswordFilter::class.java, LOGIN_PASSWORD)
            .addFilterConfig(CaptchaFilter::class.java, LOGIN_CAPTCHA)
    }

    @Bean
    open fun requestMatcher(): RequestConfigManager {
        return RequestConfigManager()
            .addPOSTURIConfig(LOGIN_PASSWORD, false)
            .addPOSTURIConfig(CAPTCHA, false)
            .addPOSTURIConfig(LOGIN_CAPTCHA, false)
            .addPOSTURIConfig(REGISTER, false)

            .addGETURIConfig(REFRESH, true)
            .addGETURIConfig(API, false)

            .addDELETEURIConfig(LOGOUT, true)
            .addDELETEURIConfig(LOGOUT, true)
    }

    @Bean
    open fun filterChain(
        http: HttpSecurity,
        securityExceptionHandler: SecurityExceptionHandler,
        signOutHandler: SignOutHandler,
        signOutSuccessHandler: SignOutSuccessHandler,
        accessRefuseHandler: AccessRefuseHandler,
        multiPasswordFilter: MultiPasswordFilter,
        requestTypeFilter: RequestTypeFilter,
        jwtFilter: JwtFilter,
        captchaFilter: CaptchaFilter
    ): SecurityFilterChain {
        return http
            .csrf(AbstractHttpConfigurer<*, *>::disable)
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .logout { logout ->
                logout
                    .logoutUrl(LOGOUT)
                    .addLogoutHandler(signOutHandler)
                    .logoutSuccessHandler(signOutSuccessHandler)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/admins/**").hasAuthority(UserConstant.Authority.ADMINISTRATION)
                    .anyRequest().authenticated()
            }
            .addFilterAt(multiPasswordFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(captchaFilter, MultiPasswordFilter::class.java)
            .addFilterBefore(jwtFilter, LogoutFilter::class.java)
            .addFilterBefore(requestTypeFilter, JwtFilter::class.java)
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint(securityExceptionHandler)
                    .accessDeniedHandler(accessRefuseHandler)
            }
            .build()
    }

    @Bean
    open fun passwordFilter(
        filterConfigManager: FilterConfigManager,
        authenticationManager: AuthenticationManager,
        authSuccessHandler: AuthSuccessHandler,
        authFailureHandler: AuthFailureHandler
    ): MultiPasswordFilter {
        return MultiPasswordFilter(filterConfigManager).apply {
            setAuthenticationManager(authenticationManager)
            setAuthenticationFailureHandler(authFailureHandler)
            setAuthenticationSuccessHandler(authSuccessHandler)
        }
    }

    @Bean
    open fun captchaFilter(
        filterConfigManager: FilterConfigManager,
        authSuccessHandler: AuthSuccessHandler,
        authenticationManager: AuthenticationManager,
        authFailureHandler: AuthFailureHandler
    ): CaptchaFilter {
        return CaptchaFilter(filterConfigManager).apply {
            setAuthenticationManager(authenticationManager)
            setAuthenticationFailureHandler(authFailureHandler)
            setAuthenticationSuccessHandler(authSuccessHandler)
        }
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun authenticationManager(
        captchaProvider: CaptchaProvider,
        passwordProvider: PasswordProvider
    ): AuthenticationManager {
        return AuthenticationTokenManager().apply {
            addProviders(captchaProvider)
            addProviders(passwordProvider)
        }
    }
}
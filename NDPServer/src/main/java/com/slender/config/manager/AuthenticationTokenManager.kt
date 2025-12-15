package com.slender.config.manager

import com.slender.exception.config.ProviderNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class AuthenticationTokenManager : AuthenticationManager {
    private val authenticationProviders = HashSet<AuthenticationProvider>()
    override fun authenticate(authentication: Authentication): Authentication {
        for (provider in authenticationProviders) {
            if (provider.supports(authentication.javaClass)) {
                return provider.authenticate(authentication)
            }
        }
        throw ProviderNotFoundException()
    }

    fun addProviders(provider: AuthenticationProvider): AuthenticationTokenManager {
        this.authenticationProviders.add(provider)
        return this
    }
}
package io.github.mrjimin.spot.global.config

import io.github.mrjimin.spot.domain.user.entity.UserRole
import io.github.mrjimin.spot.domain.user.service.CustomUserDetailsService
import io.github.mrjimin.spot.global.filter.JwtAuthenticationFilter
import io.github.mrjimin.spot.global.security.JwtTokenProvider
import io.github.mrjimin.spot.global.security.oauth2.CustomOAuth2UserService
import io.github.mrjimin.spot.global.security.oauth2.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    // private val customOAuth2UserService: CustomOAuth2UserService,
    // private val oAuth2SuccessHandler: OAuth2SuccessHandler
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtTokenProvider: JwtTokenProvider,
        customOAuth2UserService: CustomOAuth2UserService,
        oAuth2SuccessHandler: OAuth2SuccessHandler
    ): SecurityFilterChain =
        http {
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            authorizeHttpRequests {
                authorize("/api/auth/**", permitAll)
                authorize("/api/users/**", permitAll)
                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                JwtAuthenticationFilter(jwtTokenProvider)
            )

            oauth2Login {
                userInfoEndpoint { userService = customOAuth2UserService }
                authenticationSuccessHandler = oAuth2SuccessHandler
            }
        }.let { http.build() }


    @Bean
    fun roleHierarchy(): RoleHierarchy =
        RoleHierarchyImpl.withDefaultRolePrefix()
            .role(UserRole.ADMIN.name).implies(UserRole.USER.name)
            .build()

    @Bean
    fun authenticationManager(): AuthenticationManager =
        authenticationConfiguration.authenticationManager
}
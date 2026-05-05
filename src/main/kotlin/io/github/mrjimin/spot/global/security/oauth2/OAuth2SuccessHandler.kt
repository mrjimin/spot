package io.github.mrjimin.spot.global.security.oauth2

import io.github.mrjimin.spot.global.security.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    @Value($$"${app.oauth2.authorized-redirect-uri:http://localhost:3000/oauth2/redirect}")
    private val redirectUri: String
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as CustomOAuth2User
        val email = oAuth2User.user.email
        val role = "ROLE_${oAuth2User.user.role.name}"

        val tokenDto = jwtTokenProvider.createTokenDto(email, role)

        val targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("accessToken", tokenDto.accessToken)
            .queryParam("refreshToken", tokenDto.refreshToken)
            .build().toUriString()

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
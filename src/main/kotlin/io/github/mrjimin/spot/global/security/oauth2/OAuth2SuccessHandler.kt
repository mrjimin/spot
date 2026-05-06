package io.github.mrjimin.spot.global.security.oauth2

import io.github.mrjimin.spot.domain.user.service.RefreshTokenService
import io.github.mrjimin.spot.global.common.response.ResponseForm
import io.github.mrjimin.spot.global.security.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenService: RefreshTokenService,
    private val objectMapper: ObjectMapper

) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as CustomOAuth2User
        val user = oAuth2User.user

        val tokenDto = jwtTokenProvider.createTokenDto(user.email, user.role.name)

        refreshTokenService.updateRefreshToken(user.email, tokenDto.refreshToken)

        val responseData = mapOf(
            "accessToken" to tokenDto.accessToken,
            "refreshToken" to tokenDto.refreshToken,
            "nickname" to user.nickname,
            "email" to user.email
        )

        val responseForm = ResponseForm.success(
            data = responseData,
            message = "소셜 로그인에 성공하였습니다."
        )

        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_OK
        response.writer.write(objectMapper.writeValueAsString(responseForm))
    }
}
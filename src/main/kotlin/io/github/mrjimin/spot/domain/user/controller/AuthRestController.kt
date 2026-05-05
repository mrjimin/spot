package io.github.mrjimin.spot.domain.user.controller

import io.github.mrjimin.spot.domain.user.dto.*
import io.github.mrjimin.spot.domain.user.service.UserService
import io.github.mrjimin.spot.global.common.response.ResponseForm
import io.github.mrjimin.spot.global.exception.ErrorCode
import io.github.mrjimin.spot.global.exception.SpotException
import io.github.mrjimin.spot.global.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthRestController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseForm<Nothing> {
        userService.register(request)
        return ResponseForm.success("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseForm<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userService.findByEmail(authentication.name)
            ?: throw SpotException(ErrorCode.USER_NOT_FOUND)

        val role = authentication.authorities.first().authority!!
        val tokenDto = jwtTokenProvider.createTokenDto(user.email, role)

        return ResponseForm.success(LoginResponse(user.email, user.nickname, tokenDto))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody request: RefreshRequest): ResponseForm<TokenDto> {
        if (!jwtTokenProvider.validateToken(request.refreshToken)) {
            throw IllegalArgumentException("리프레시 토큰 유효성 검사 실패")
        }

        val email = jwtTokenProvider.getEmail(request.refreshToken)
        val user = userService.findByEmail(email) ?: throw IllegalArgumentException("유저 없음")
        val newTokenDto = jwtTokenProvider.createTokenDto(email, user.role.name)
        return ResponseForm.success(newTokenDto)
    }
}
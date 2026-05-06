package io.github.mrjimin.spot.domain.user.controller

import io.github.mrjimin.spot.domain.user.dto.*
import io.github.mrjimin.spot.domain.user.service.RefreshTokenService
import io.github.mrjimin.spot.domain.user.service.UserService
import io.github.mrjimin.spot.global.common.response.ResponseForm
import io.github.mrjimin.spot.global.exception.ErrorCode
import io.github.mrjimin.spot.global.exception.SpotException
import io.github.mrjimin.spot.global.security.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
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
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseForm<Unit> {
        userService.register(request)
        return ResponseForm.success("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseForm<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userService.findByEmail(authentication.name) ?: throw SpotException(ErrorCode.USER_NOT_FOUND)
        val tokenDto = jwtTokenProvider.createTokenDto(user.email, user.role.name)

        refreshTokenService.updateRefreshToken(user.email, tokenDto.refreshToken)
        return ResponseForm.success(LoginResponse(user.email, user.nickname, tokenDto))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody request: RefreshRequest): ResponseForm<TokenDto> {
        if (!jwtTokenProvider.validateToken(request.refreshToken)) {
            throw SpotException(ErrorCode.UNAUTHORIZED)
        }

        val email = jwtTokenProvider.getEmail(request.refreshToken)
        val user = userService.findByEmail(email) ?: throw SpotException(ErrorCode.USER_NOT_FOUND)
        val newTokenDto = jwtTokenProvider.createTokenDto(email, user.role.name)

        refreshTokenService.updateRefreshToken(email, newTokenDto.refreshToken)
        return ResponseForm.success(newTokenDto)
    }

//    @PostMapping("/logout")
//    fun logout(request: HttpServletRequest): ResponseForm<Unit> {
//        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
//
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            val email = jwtTokenProvider.getEmail(token)
//            refreshTokenService.deleteByEmail(email)
//        }
//
//        return ResponseForm.success("로그아웃 성공")
//    }
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseForm<Unit> {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)


            if (jwtTokenProvider.validateToken(token)) {
                val email = jwtTokenProvider.getEmail(token)
                refreshTokenService.deleteByEmail(email)
            }
        }

        return ResponseForm.success("로그아웃 성공")
    }
}
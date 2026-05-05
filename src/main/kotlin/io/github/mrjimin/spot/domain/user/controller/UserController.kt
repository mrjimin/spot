package io.github.mrjimin.spot.domain.user.controller

import io.github.mrjimin.spot.domain.user.dto.UserResponse
import io.github.mrjimin.spot.domain.user.dto.toResponse
import io.github.mrjimin.spot.domain.user.entity.User
import io.github.mrjimin.spot.domain.user.service.UserService
import io.github.mrjimin.spot.global.common.response.ResponseForm
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getMyInfo(@AuthenticationPrincipal userDetails: UserDetails?): ResponseForm<UserResponse> {
        val email = userDetails?.username ?: throw IllegalArgumentException("인증되지 않은 사용자입니다.")
        val user = userService.findByEmail(email) ?: throw IllegalArgumentException("유저 정보를 찾을 수 없습니다.")
        return ResponseForm.success(user.toResponse())
    }

    @GetMapping
    fun getAllUsers(): ResponseForm<List<UserResponse>> {
        val users = userService.findAll().map { it.toResponse() }
        return ResponseForm.success(users)
    }
}
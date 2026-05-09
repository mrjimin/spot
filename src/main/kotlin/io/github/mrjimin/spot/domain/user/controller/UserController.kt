package io.github.mrjimin.spot.domain.user.controller

import io.github.mrjimin.spot.domain.user.dto.UserResponse
import io.github.mrjimin.spot.domain.user.dto.toResponse
import io.github.mrjimin.spot.domain.user.service.UserService
import io.github.mrjimin.spot.global.common.response.ResponseForm
import io.github.mrjimin.spot.global.exception.ErrorCode
import io.github.mrjimin.spot.global.exception.SpotException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getMyInfo(@AuthenticationPrincipal userDetails: UserDetails?): ResponseForm<UserResponse> {
        val email = userDetails?.username ?: throw SpotException(ErrorCode.USER_NOT_FOUND)
        val user = userService.findByEmail(email) ?: throw SpotException(ErrorCode.USER_NOT_FOUND)
        return ResponseForm.success(user.toResponse())
    }

    @GetMapping
    fun getUsers(
        @RequestParam(required = false) uuid: UUID?,
        @RequestParam(required = false) email: String?
    ): ResponseForm<List<UserResponse>> {
        val result = when {
            uuid != null -> userService.findById(uuid)?.let { listOf(it) }
            email != null -> userService.findByEmail(email)?.let { listOf(it) }
            else -> userService.findAll()
        }

        val responses = result?.takeIf { it.isNotEmpty() }?.map { it.toResponse() }
            ?: throw SpotException(ErrorCode.USER_NOT_FOUND)

        return ResponseForm.success(responses)
    }

//    @GetMapping("email/{email}")
//    fun getUserByEmail(@PathVariable email: String): ResponseForm<UserResponse> {
//        val user = userService.findByEmail(email) ?: throw SpotException(ErrorCode.USER_NOT_FOUND)
//        return ResponseForm.success(user.toResponse())
//    }
}
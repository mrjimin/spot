package io.github.mrjimin.spot.domain.user.dto

import io.github.mrjimin.spot.domain.user.entity.LoginProvider
import io.github.mrjimin.spot.domain.user.entity.User
import io.github.mrjimin.spot.domain.user.entity.UserRole
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val nickname: String,
    val provider: LoginProvider,
    val role: UserRole
)

fun User.toResponse() = UserResponse(
    this.id!!,
    this.email,
    this.nickname,
    this.provider,
    this.role
)
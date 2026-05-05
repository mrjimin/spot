package io.github.mrjimin.spot.domain.user.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val nickname: String? = null
)
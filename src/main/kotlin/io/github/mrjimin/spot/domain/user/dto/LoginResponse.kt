package io.github.mrjimin.spot.domain.user.dto

data class LoginResponse(
    val email: String,
    val nickname: String,
    val tokens: TokenDto
)
package io.github.mrjimin.spot.domain.user.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)
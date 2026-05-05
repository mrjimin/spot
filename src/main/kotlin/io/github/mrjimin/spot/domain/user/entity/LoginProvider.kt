package io.github.mrjimin.spot.domain.user.entity

enum class LoginProvider(val registrationId: String) {
    LOCAL("local"),
    GOOGLE("google"),
    KAKAO("kakao");

    companion object {
        fun fromString(id: String): LoginProvider {
            return entries.find { it.registrationId == id.lowercase() }
                ?: throw IllegalArgumentException("지원하지 않는 로그인 제공자입니다: $id")
        }
    }
}
package io.github.mrjimin.spot.global.security.oauth2

import io.github.mrjimin.spot.domain.user.entity.LoginProvider

object OAuth2UserInfoFactory {
    fun getReceiver(provider: LoginProvider, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (provider) {
            LoginProvider.GOOGLE -> GoogleUserInfo(attributes)
            else -> throw IllegalArgumentException("유효하지 않은 소셜 로그인입니다.")
        }
    }
}
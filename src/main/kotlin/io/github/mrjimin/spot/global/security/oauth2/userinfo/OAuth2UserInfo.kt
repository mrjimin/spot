package io.github.mrjimin.spot.global.security.oauth2.userinfo

abstract class OAuth2UserInfo(
    protected val attributes: Map<String, Any>
) {
    abstract fun getProviderId(): String
    abstract fun getEmail(): String
    abstract fun getNickname(): String
}
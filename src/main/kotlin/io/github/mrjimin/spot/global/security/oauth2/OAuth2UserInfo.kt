package io.github.mrjimin.spot.global.security.oauth2

abstract class OAuth2UserInfo(
    protected val attributes: Map<String, Any>
) {
    abstract fun getProviderId(): String
    abstract fun getEmail(): String
    abstract fun getNickname(): String
}

class GoogleUserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getProviderId(): String = attributes["sub"] as String
    override fun getEmail(): String = attributes["email"] as String
    override fun getNickname(): String = attributes["name"] as String
}
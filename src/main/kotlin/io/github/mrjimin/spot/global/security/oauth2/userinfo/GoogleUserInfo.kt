package io.github.mrjimin.spot.global.security.oauth2.userinfo

class GoogleUserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getProviderId(): String = attributes["sub"] as String
    override fun getEmail(): String = attributes["email"] as String
    override fun getNickname(): String = attributes["name"] as String
}

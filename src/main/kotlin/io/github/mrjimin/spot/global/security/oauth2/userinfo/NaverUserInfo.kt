package io.github.mrjimin.spot.global.security.oauth2.userinfo

class NaverUserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    private val response: Map<String, String> = attributes["response"] as Map<String, String>

    override fun getProviderId(): String = response["id"] as String
    override fun getEmail(): String = response["email"] as String
    override fun getNickname(): String = response["name"] as String
}

package io.github.mrjimin.spot.global.security.oauth2

import io.github.mrjimin.spot.domain.user.entity.LoginProvider
import io.github.mrjimin.spot.domain.user.service.UserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
    private val userService: UserService
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val provider = LoginProvider.fromString(userRequest.clientRegistration.registrationId)
        val userInfo = OAuth2UserInfoFactory.getReceiver(provider, oAuth2User.attributes)

        val user = userService.getOrCreateOAuth2User(userInfo, provider)

        return CustomOAuth2User(user, oAuth2User.attributes)
    }
}
package io.github.mrjimin.spot.domain.user.service

import io.github.mrjimin.spot.domain.user.dto.RegisterRequest
import io.github.mrjimin.spot.domain.user.entity.LoginProvider
import io.github.mrjimin.spot.domain.user.entity.User
import io.github.mrjimin.spot.domain.user.entity.UserRole
import io.github.mrjimin.spot.domain.user.repository.UserRepository
import io.github.mrjimin.spot.global.security.oauth2.userinfo.OAuth2UserInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAll(): List<User> = userRepository.findAll()
    fun findById(id: UUID): User? = userRepository.findByIdOrNull(id)
    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    @Transactional
    fun register(request: RegisterRequest): User {
        val newUser = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname ?: request.email.split("@")[0],
            provider = LoginProvider.LOCAL
        )
        return create(newUser)
    }

    @Transactional
    fun getOrCreateOAuth2User(userInfo: OAuth2UserInfo, provider: LoginProvider): User {
        return userRepository.findByProviderAndProviderId(provider, userInfo.getProviderId())
            ?: userRepository.findByEmail(userInfo.getEmail())?.apply {
                this.provider = provider
                this.providerId = userInfo.getProviderId()
            } ?: createForSocial(userInfo, provider)
    }

    private fun createForSocial(userInfo: OAuth2UserInfo, provider: LoginProvider): User {
        return userRepository.save(User(
            email = userInfo.getEmail(),
            password = null,
            nickname = userInfo.getNickname(),
            provider = provider,
            providerId = userInfo.getProviderId(),
            role = UserRole.USER
        ))
    }

    @Transactional
    fun create(user: User): User {
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        return userRepository.save(user)
    }

    @Transactional
    fun updateNickname(email: String, newNickname: String) {
        val user = findByEmail(email) ?: throw IllegalArgumentException("유저 없음")
        user.nickname = newNickname
    }
}
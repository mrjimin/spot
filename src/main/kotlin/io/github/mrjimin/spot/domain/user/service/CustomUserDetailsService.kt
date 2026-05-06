package io.github.mrjimin.spot.domain.user.service

import io.github.mrjimin.spot.domain.user.repository.UserRepository
import io.github.mrjimin.spot.global.exception.ErrorCode
import io.github.mrjimin.spot.global.exception.SpotException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw SpotException(ErrorCode.USER_NOT_FOUND)

        return User
            .withUsername(user.email)
            .password(user.password ?: "")
            .authorities(user.role.name)
            .build()
    }
}
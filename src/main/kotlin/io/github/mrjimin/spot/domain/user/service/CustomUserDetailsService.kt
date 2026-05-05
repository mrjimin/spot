package io.github.mrjimin.spot.domain.user.service

import io.github.mrjimin.spot.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as SpringUser

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("유저를 찾을 수 없습니다: $email")

        return SpringUser
            .withUsername(user.email)
            .password(user.password ?: "")
            .authorities(user.role.name)
            .build()
    }
}
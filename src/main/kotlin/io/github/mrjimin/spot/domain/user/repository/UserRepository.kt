package io.github.mrjimin.spot.domain.user.repository

import io.github.mrjimin.spot.domain.user.entity.LoginProvider
import io.github.mrjimin.spot.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun findByProviderAndProviderId(provider: LoginProvider, providerId: String): User?
    fun existsByEmail(email: String): Boolean
}
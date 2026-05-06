package io.github.mrjimin.spot.domain.user.repository

import io.github.mrjimin.spot.domain.user.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByEmail(email: String): RefreshToken?
    fun deleteByEmail(email: String)
}
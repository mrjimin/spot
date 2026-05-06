package io.github.mrjimin.spot.domain.user.service

import io.github.mrjimin.spot.domain.user.entity.RefreshToken
import io.github.mrjimin.spot.domain.user.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    @Transactional
    fun updateRefreshToken(email: String, newToken: String) {
        val refreshToken = refreshTokenRepository.findByEmail(email)
            ?.apply { token = newToken }
            ?: RefreshToken(email = email, token = newToken)

        refreshTokenRepository.save(refreshToken)
    }

    fun validateRefreshToken(email: String, token: String): Boolean {
        val storedToken = refreshTokenRepository.findByEmail(email)
        return storedToken?.token == token
    }

    @Transactional
    fun deleteByEmail(email: String) {
        refreshTokenRepository.deleteByEmail(email)
    }
}
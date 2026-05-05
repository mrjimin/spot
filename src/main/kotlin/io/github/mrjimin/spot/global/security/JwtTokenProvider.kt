package io.github.mrjimin.spot.global.security

import io.github.mrjimin.spot.domain.user.dto.TokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {
    private val secretKey = Jwts.SIG.HS256.key().build()
    private val accessTokenValidity = 3600000L // 1시간
    private val refreshTokenValidity = 1209600000L // 2주

    fun createTokenDto(email: String, role: String): TokenDto {
        val now = Date().time
        val accessToken = createToken(email, role, accessTokenValidity)
        val refreshToken = createToken(email, null, refreshTokenValidity)

        return TokenDto(
            accessToken,
            refreshToken,
            now + accessTokenValidity
        )
    }

    private fun createToken(email: String, role: String?, validityTime: Long): String {
        val now = Date()
        val builder = Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(Date(now.time + validityTime))
            .signWith(secretKey)

        role?.let { builder.claim("role", it) }
        return builder.compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)

        val role = claims["role"]?.toString() ?: "USER"
        val authorities = listOf(SimpleGrantedAuthority(role))

        val principal = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun getEmail(token: String): String =
        Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).payload.subject

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    private fun getClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
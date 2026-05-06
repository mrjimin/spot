package io.github.mrjimin.spot.global.filter

import io.github.mrjimin.spot.global.security.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    // private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = resolveToken(request)

//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            val email = jwtTokenProvider.getEmail(token)
//            val userDetails = userDetailsService.loadUserByUsername(email)
//            val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//            SecurityContextHolder.getContext().authentication = auth
//        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}
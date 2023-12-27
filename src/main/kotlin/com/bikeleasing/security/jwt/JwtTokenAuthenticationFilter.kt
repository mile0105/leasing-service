package com.bikeleasing.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)
        var username: String? = null
        if (authorizationHeader == null || !authorizationHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        val token = authorizationHeader.replace(ACCESS_TOKEN_PREFIX, "")
        try {
            val tokenType: String = jwtUtils.getTokenType(token)
            if (ACCESS_TOKEN_PREFIX == tokenType) {
                username = jwtUtils.getUsernameFromToken(token)
            }
        } catch (e: IllegalArgumentException) {
            logger.warn("Unable to get JWT")
        } catch (e: ExpiredJwtException) {
            logger.warn("JWT is expired")
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (jwtUtils.validateToken(token, userDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        chain.doFilter(request, response)
    }

    companion object {
        const val ACCESS_TOKEN_PREFIX = "Bearer "
        const val AUTHORIZATION_HEADER = "Authorization"
        const val REFRESH_TOKEN_PREFIX = "Refresh "
    }
}

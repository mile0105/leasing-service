package com.bikeleasing.security

import com.bikeleasing.error.exceptions.BadRequestException
import com.bikeleasing.security.jwt.JwtTokenAuthenticationFilter.Companion.ACCESS_TOKEN_PREFIX
import com.bikeleasing.security.jwt.JwtTokenAuthenticationFilter.Companion.AUTHORIZATION_HEADER
import com.bikeleasing.security.jwt.JwtUtils
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

@Service
class PrincipalService(
    private val jwtUtils: JwtUtils
) {

    fun getLoggedInUserId(): Long {
        val authorizationHeader: String = getCurrentHttpRequest().getHeader(AUTHORIZATION_HEADER)
        val token: String = authorizationHeader.replace(ACCESS_TOKEN_PREFIX, "")
        return jwtUtils.getUserIdFromToken(token)
    }


    private fun getCurrentHttpRequest(): HttpServletRequest {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes is ServletRequestAttributes) {
            return requestAttributes.request
        }
        throw BadRequestException("Invalid Request")
    }
}


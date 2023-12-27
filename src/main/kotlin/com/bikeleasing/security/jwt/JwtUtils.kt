package com.bikeleasing.security.jwt

import com.bikeleasing.security.jwt.JwtTokenAuthenticationFilter.Companion.ACCESS_TOKEN_PREFIX
import com.bikeleasing.security.jwt.JwtTokenAuthenticationFilter.Companion.REFRESH_TOKEN_PREFIX
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.sql.Date
import java.time.Instant
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils {

    private var jwtSigningKey: String = "IQ09J2BdDuc3lSKUJlQAp8uhCXRq+s2EucsBOb9rfjo=}"

    private var accessTokenValiditySeconds: Int = 43200
    private var refreshTokenValiditySeconds: Int = 259200

    fun getUsernameFromToken(token: String): String {
        val claims: Claims = getAllClaimsFromToken(token)
        return claims.subject
    }

    fun getUserIdFromToken(token: String): Long {
        val claims: Claims = getAllClaimsFromToken(token)
        val integerClaim = claims.get("user_id", Integer::class.java)
        return integerClaim.toLong()
    }

    fun generateAccessToken(authentication: Authentication, userId: Long): String {
        val now = Instant.now()
        return Jwts.builder()
            .setSubject(authentication.name)
            .setHeaderParam("type", ACCESS_TOKEN_PREFIX)
            .claim("authorities", authentication.authorities.stream()
                .map<String> { obj: GrantedAuthority -> obj.authority }.collect(Collectors.toList())
            )
            .claim("user_id", userId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(accessTokenValiditySeconds.toLong())))
            .signWith(keygen())
            .compact()
    }

    fun generateRefreshToken(authentication: Authentication, userId: Long): String {
        val now = Instant.now()
        return Jwts.builder()
            .setSubject(authentication.name)
            .setHeaderParam("type", REFRESH_TOKEN_PREFIX)
            .claim("authorities", authentication.authorities.stream()
                .map<String> { obj: GrantedAuthority -> obj.authority }.collect(Collectors.toList())
            )
            .claim("user_id", userId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(refreshTokenValiditySeconds.toLong())))
            .signWith(keygen())
            .compact()
    }

    fun generateAccessToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
            .setClaims(claims)
            .setHeaderParam("type", ACCESS_TOKEN_PREFIX)
            .setSubject(userDetails.username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(accessTokenValiditySeconds.toLong())))
            .signWith(keygen())
            .compact()
    }


    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(keygen())
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun generateRefreshToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
            .setClaims(claims)
            .setHeaderParam("type", REFRESH_TOKEN_PREFIX)
            .setSubject(userDetails.username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(refreshTokenValiditySeconds.toLong())))
            .signWith(keygen())
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return userDetails.username == username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDate(token)
        return expiration.isBefore(Instant.now())
    }


    private fun keygen(): Key {
        return SecretKeySpec(
            jwtSigningKey.toByteArray(),
            SignatureAlgorithm.HS256.jcaName
        )
    }

    private fun getExpirationDate(token: String): Instant {
        val claims: Claims = getAllClaimsFromToken(token)
        return claims.expiration.toInstant()
    }

    fun getTokenType(token: String): String {
        return Jwts.parserBuilder().setSigningKey(keygen()).build().parseClaimsJws(token).header["type"] as String
    }
}

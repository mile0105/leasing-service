package com.bikeleasing.security.jwt

data class CustomJwtToken(
    val accessToken: String,
    val refreshToken: String
)

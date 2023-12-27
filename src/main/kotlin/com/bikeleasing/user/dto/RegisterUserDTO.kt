package com.bikeleasing.user.dto

data class RegisterUserDTO(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

package com.bikeleasing.user.service

import com.bikeleasing.error.exceptions.BadRequestException
import com.bikeleasing.error.exceptions.UnauthorizedException
import com.bikeleasing.user.dto.RegisterUserDTO
import com.bikeleasing.user.model.User
import com.bikeleasing.user.repository.UserRepository
import com.bikeleasing.security.jwt.CustomJwtToken
import com.bikeleasing.security.jwt.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils
) {

    fun registerUser(userDTO: RegisterUserDTO) {

        val existingUserByEmail = userRepository.findByEmail(userDTO.email)

        if (existingUserByEmail != null) {
            throw BadRequestException(
                message = "User with email exists"
            )
        }

        val user = User(
            id = null,
            firstName = userDTO.firstName,
            lastName = userDTO.lastName,
            email = userDTO.email,
            password = passwordEncoder.encode(userDTO.password)
        )

        userRepository.save(user)
    }

    fun login(email: String, password: String): CustomJwtToken {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )

        val userId: Long = userRepository.findByEmail(email)?.id ?: throw UnauthorizedException("Unauthorized")

        val accessToken: String = jwtUtils.generateAccessToken(authentication, userId)
        val refreshToken: String = jwtUtils.generateRefreshToken(authentication, userId)

        return CustomJwtToken(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

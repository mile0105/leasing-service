package com.bikeleasing.user

import com.bikeleasing.user.dto.LoginUserDTO
import com.bikeleasing.user.dto.RegisterUserDTO
import com.bikeleasing.security.jwt.CustomJwtToken
import com.bikeleasing.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginUserDTO: LoginUserDTO): ResponseEntity<CustomJwtToken> {
        val jwtToken = userService.login(
            email = loginUserDTO.email,
            password = loginUserDTO.password
        )

        return ResponseEntity.ok().body(jwtToken)
    }

    @PostMapping("/register")
    fun register(@RequestBody registerUserDTO: RegisterUserDTO): ResponseEntity<Void> {
        userService.registerUser(userDTO = registerUserDTO)

        return ResponseEntity(HttpStatus.CREATED)
    }
}

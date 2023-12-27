package com.bikeleasing.error.exceptions

import org.springframework.http.HttpStatus

class UnauthorizedException(
    override val message: String
): ApiException() {

    override val statusCode: HttpStatus = HttpStatus.UNAUTHORIZED
}

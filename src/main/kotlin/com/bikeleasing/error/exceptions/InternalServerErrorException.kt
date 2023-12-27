package com.bikeleasing.error.exceptions

import org.springframework.http.HttpStatus

class InternalServerErrorException(
    override val message: String
): ApiException() {

    override val statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
}

package com.bikeleasing.error.exceptions

import org.springframework.http.HttpStatus

class BadRequestException(
    override val message: String
): ApiException() {

    override val statusCode: HttpStatus = HttpStatus.BAD_REQUEST

}

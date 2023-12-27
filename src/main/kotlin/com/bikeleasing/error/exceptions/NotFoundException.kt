package com.bikeleasing.error.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(
    override val message: String
): ApiException() {

    override val statusCode: HttpStatus = HttpStatus.NOT_FOUND
}

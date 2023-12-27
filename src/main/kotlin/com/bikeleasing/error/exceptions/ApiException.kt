package com.bikeleasing.error.exceptions

import org.springframework.http.HttpStatus

abstract class ApiException : RuntimeException() {
    abstract val statusCode: HttpStatus


    fun getErrorMessage(): String? {
        return this.message
    }

}


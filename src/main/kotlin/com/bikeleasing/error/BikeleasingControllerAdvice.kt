package com.bikeleasing.error

import com.bikeleasing.error.exceptions.ApiException
import com.bikeleasing.error.model.ErrorMessage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class BikeleasingControllerAdvice {

    @ExceptionHandler(value = [ApiException::class])
    fun onApiException(exception: ApiException): ResponseEntity<ErrorMessage> {
        val message = ErrorMessage(
            message = exception.getErrorMessage() ?: ""
        )

        return ResponseEntity(message, exception.statusCode)
    }
}

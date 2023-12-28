package com.bikeleasing.reservation.dto

import com.bikeleasing.error.exceptions.BadRequestException
import java.time.Instant

data class ReservationRequestDTO(
    val bikeId: Long,
    val validFrom: Instant,
    val validTo: Instant
) {

    fun validate() {
        val currentTimestamp = Instant.now()
        if(validFrom.isAfter(validTo)) {
            throw BadRequestException("Invalid date range")
        }

        if(currentTimestamp.isAfter(validFrom) || currentTimestamp.isAfter(validTo)) {
            throw BadRequestException("Reservation must be made in the future")
        }
    }
}
package com.bikeleasing.reservation.dto

import com.bikeleasing.bike.dto.BikeResponseDTO
import java.time.Instant

data class ReservationDetailsDTO(
    val id: Long?,
    val bike: BikeResponseDTO,
    val owner: String,
    val validFrom: Instant,
    val validTo: Instant
)
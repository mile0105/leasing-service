package com.bikeleasing.bike.dto

data class BikeResponseDTO(
    val make: String,
    val model: String,
    val ownedBy: String,
    val id: Long?
)

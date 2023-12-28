package com.bikeleasing.reservation.service

import com.bikeleasing.bike.model.Bike
import com.bikeleasing.bike.repository.BikeRepository
import com.bikeleasing.error.exceptions.BadRequestException
import com.bikeleasing.error.exceptions.NotFoundException
import com.bikeleasing.reservation.dto.ReservationDetailsDTO
import com.bikeleasing.reservation.dto.ReservationRequestDTO
import com.bikeleasing.reservation.model.Reservation
import com.bikeleasing.reservation.repository.ReservationRepository
import com.bikeleasing.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReservationService(
    private val userRepository: UserRepository,
    private val bikeRepository: BikeRepository,
    private val reservationRepository: ReservationRepository
) {

    fun getReservationsFromUser(loggedInUserId: Long): List<ReservationDetailsDTO> {
        val user = userRepository.findByIdOrNull(loggedInUserId)!!
        return reservationRepository.findAllByOwner(user = user).map { it.toReservationDetailsDTO() }
    }

    fun makeReservation(loggedInUserId: Long, reservationRequestDTO: ReservationRequestDTO) {
        val user = userRepository.findByIdOrNull(loggedInUserId)!!
        val bike = bikeRepository.findById(reservationRequestDTO.bikeId).map { it }.orElseThrow {
            NotFoundException(
                message = "Bike not found"
            )
        }

        checkBikeAvailability(
            bike = bike,
            validFrom = reservationRequestDTO.validFrom,
            validTo = reservationRequestDTO.validTo
        )

        val reservation = Reservation(
            id = null,
            bike = bike,
            owner = user,
            validFrom = reservationRequestDTO.validFrom,
            validTo = reservationRequestDTO.validTo
        )

        reservationRepository.save(reservation)
    }

    private fun checkBikeAvailability(bike: Bike, validFrom: Instant, validTo: Instant) {
        if(bike.reservations.any { !(validFrom.isAfter(it.validTo) || it.validFrom.isAfter(validTo)) }) {
            throw BadRequestException(
                message = "Bike not available during that time"
            )
        }
    }


}

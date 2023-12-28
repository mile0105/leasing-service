package com.bikeleasing.reservation

import com.bikeleasing.reservation.dto.ReservationDetailsDTO
import com.bikeleasing.reservation.dto.ReservationRequestDTO
import com.bikeleasing.reservation.service.ReservationService
import com.bikeleasing.security.PrincipalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reservations")
class ReservationController(
    private val reservationService: ReservationService,
    private val principalService: PrincipalService
) {

    @GetMapping("/my")
    fun getMyReservations(): ResponseEntity<List<ReservationDetailsDTO>> {
        val loggedInUserId = principalService.getLoggedInUserId()
        val reservations = reservationService.getReservationsFromUser(
            loggedInUserId = loggedInUserId
        )
        return ResponseEntity.ok(reservations)
    }

    @PostMapping
    fun makeReservation(
        @RequestBody reservationRequestDTO: ReservationRequestDTO
    ): ResponseEntity<Void> {
        reservationRequestDTO.validate()

        val loggedInUserId = principalService.getLoggedInUserId()
        reservationService.makeReservation(
            loggedInUserId = loggedInUserId,
            reservationRequestDTO = reservationRequestDTO
        )

        return ResponseEntity(HttpStatus.CREATED)
    }
}

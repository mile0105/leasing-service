package com.bikeleasing.reservation.repository

import com.bikeleasing.reservation.model.Reservation
import com.bikeleasing.user.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository: CrudRepository<Reservation, Long> {

    fun findAllByOwner(user: User): List<Reservation>
}
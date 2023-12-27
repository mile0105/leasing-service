package com.bikeleasing.reservation.model

import com.bikeleasing.bike.model.Bike
import com.bikeleasing.user.model.User
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "reservations")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @ManyToOne
    @JoinColumn(name = "bike_id")
    val bike: Bike,

    @ManyToOne
    @JoinColumn(name = "reservation_owner_id")
    val owner: User,
    val validFrom: Instant,
    val validTo: Instant
)

package com.bikeleasing.bike.model

import com.bikeleasing.bike.dto.BikeResponseDTO
import com.bikeleasing.reservation.model.Reservation
import com.bikeleasing.user.model.User
import javax.persistence.*

@Entity
@Table(name = "bikes")
data class Bike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val make: String,
    val model: String,

    @ManyToOne
    @JoinColumn(name = "bike_owner_id")
    val owner: User,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bike")
    val reservations: MutableList<Reservation> = mutableListOf()
) {

    fun toResponseDTO(): BikeResponseDTO {
        return BikeResponseDTO(
            make = this.make,
            model = this.model,
            ownedBy = this.owner.fullName(),
            id = id
        )
    }
}

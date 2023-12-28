package com.bikeleasing.bike.repository

import com.bikeleasing.bike.model.Bike
import com.bikeleasing.user.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BikeRepository: CrudRepository<Bike, Long> {

    fun getBikesOwnedByOwner(owner: User): List<Bike>

    fun getBikesOwnedByOwnerNot(owner: User): List<Bike>
}

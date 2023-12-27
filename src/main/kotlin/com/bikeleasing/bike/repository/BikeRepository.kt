package com.bikeleasing.bike.repository

import com.bikeleasing.bike.model.Bike
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BikeRepository: CrudRepository<Bike, Long> {

    @Query(value = "SELECT * FROM BIKES WHERE bike_owner_id = :bikeOwnerId", name = "getBikesOwnedByUserCustom")
    fun getBikesOwnedByOwner(@Param("bikeOwnerId") bikeOwnerId: Long): List<Bike>

    @Query(value ="SELECT b.* FROM BIKES b WHERE b.bike_owner_id <> :bikeOwnerId " +
            "AND NOT EXISTS(SELECT * FROM RESERVATIONS r WHERE r.bike_id = b.id)", name = "getBikesNotOwnedByUserCustom")
    fun getBikesNotOwnedByOwner(@Param("bikeOwnerId") bikeOwnerId: Long): List<Bike>
}

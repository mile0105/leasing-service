package com.bikeleasing.bike.service

import com.bikeleasing.bike.dto.BikeResponseDTO
import com.bikeleasing.bike.dto.CreateBikeRequestDTO
import com.bikeleasing.bike.model.Bike
import com.bikeleasing.bike.repository.BikeRepository
import com.bikeleasing.error.exceptions.NotFoundException
import com.bikeleasing.error.exceptions.UnauthorizedException
import com.bikeleasing.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BikeService(
    private val bikeRepository: BikeRepository,
    private val userRepository: UserRepository,
) {

    fun createBike(createBikeRequestDTO: CreateBikeRequestDTO, loggedInUserId: Long): BikeResponseDTO {

        val loggedInUser = userRepository.findById(loggedInUserId).map { it }.orElseThrow {
            UnauthorizedException("")
        }

        val bike = Bike(
            id = null,
            make = createBikeRequestDTO.make,
            model = createBikeRequestDTO.model,
            owner = loggedInUser
        )

        val dbBike = bikeRepository.save(bike)

        return dbBike.toResponseDTO()
    }

    fun getBikesNotOwnedByUserIdAvailableForReservation(loggedInUserId: Long): List<BikeResponseDTO> {
        val user = userRepository.findByIdOrNull(loggedInUserId)!!
        return bikeRepository.getBikesOwnedByOwnerNot(
            user
        ).map {
            it.toResponseDTO()
        }
    }

    fun getBikesOwnedByUserId(loggedInUserId: Long): List<BikeResponseDTO> {
        val user = userRepository.findByIdOrNull(loggedInUserId)!!
        return bikeRepository.getBikesOwnedByOwner(
            user
        ).map {
            it.toResponseDTO()
        }
    }

    fun deleteBike(bikeId: Long, loggedInUserId: Long) {
        val bike = bikeRepository.findById(bikeId).map { it }.orElseThrow {
            NotFoundException("Bike not found")
        }

        if (bike.owner.id != loggedInUserId) {
            throw UnauthorizedException("")
        }

        bikeRepository.delete(bike)
    }
}

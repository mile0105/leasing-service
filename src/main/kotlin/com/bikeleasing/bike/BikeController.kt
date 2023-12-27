package com.bikeleasing.bike

import com.bikeleasing.bike.dto.BikeResponseDTO
import com.bikeleasing.bike.dto.CreateBikeRequestDTO
import com.bikeleasing.bike.service.BikeService
import com.bikeleasing.security.PrincipalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bikes")
class BikeController(
    private val bikeService: BikeService,
    private val principalService: PrincipalService
) {

    @PostMapping
    fun createBike(
        @RequestBody createBikeRequestDTO: CreateBikeRequestDTO
    ): ResponseEntity<BikeResponseDTO> {
        val response = bikeService.createBike(
            createBikeRequestDTO = createBikeRequestDTO,
            loggedInUserId = principalService.getLoggedInUserId()
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAvailableBikesForReservation(): ResponseEntity<List<BikeResponseDTO>> {
        val response = bikeService.getBikesNotOwnedByUserIdAvailableForReservation(
            loggedInUserId = principalService.getLoggedInUserId()
        )

        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteBike(@PathVariable id: Long): ResponseEntity<Void> {
        bikeService.deleteBike(
            bikeId = id,
            loggedInUserId = principalService.getLoggedInUserId()
        )
        return ResponseEntity.ok().build()
    }

    @GetMapping("/my")
    fun getMyBikes(): ResponseEntity<List<BikeResponseDTO>> {
        val response = bikeService.getBikesOwnedByUserId(
            loggedInUserId = principalService.getLoggedInUserId()
        )

        return ResponseEntity.ok(response)
    }
}

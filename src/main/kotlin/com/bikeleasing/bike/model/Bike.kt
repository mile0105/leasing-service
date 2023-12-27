package com.bikeleasing.bike.model

import com.bikeleasing.user.model.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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
    val owner: User
)

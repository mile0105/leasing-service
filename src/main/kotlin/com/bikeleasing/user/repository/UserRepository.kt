package com.bikeleasing.user.repository

import com.bikeleasing.user.model.User
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {

    @Query("SELECT * FROM USERS WHERE email = :email")
    fun findByEmail(@Param("email") email: String): User?

}

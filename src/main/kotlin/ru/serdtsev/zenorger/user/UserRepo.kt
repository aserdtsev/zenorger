package ru.serdtsev.zenorger.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo: JpaRepository<User, UUID> {
    fun findByLogin(login: String): User?
}
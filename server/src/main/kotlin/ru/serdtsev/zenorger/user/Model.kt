package ru.serdtsev.zenorger.user

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "service_user")
data class User (
    @Id val id: UUID,
    var login: String,
    var password: String
)
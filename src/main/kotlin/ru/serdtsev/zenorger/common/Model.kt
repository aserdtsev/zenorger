package ru.serdtsev.zenorger.common

import ru.serdtsev.zenorger.user.User
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Organizer(
        @Id val id: UUID? = null,
        val createdAt: OffsetDateTime? = null,
        @ManyToOne val user: User? = null,
        var name: String? = null
)
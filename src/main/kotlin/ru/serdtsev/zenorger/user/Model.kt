package ru.serdtsev.zenorger.user

import ru.serdtsev.zenorger.common.Organizer
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "service_user")
data class User(
    @Id val id: UUID? = null,
    val createdAt: OffsetDateTime? = null,
    val login: String? = null,
    val password: String? = null) {
    @OneToMany val organizers: List<Organizer> = emptyList()
}

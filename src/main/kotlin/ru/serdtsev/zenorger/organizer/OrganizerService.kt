package ru.serdtsev.zenorger.organizer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import ru.serdtsev.zenorger.common.ZenorgerException
import ru.serdtsev.zenorger.user.User
import java.util.*

@Service
class OrganizerService(val organizerRepo: OrganizerRepo) {
    @Transactional
    fun createOrganizer(id: UUID, user: User): Organizer {
        val organizer = Organizer(id, user, "Default organizer")
        organizerRepo.save(organizer)
        return organizer
    }

    fun getDefaultOrganizerByUser(user: User): Organizer = organizerRepo.findByUser(user).firstOrNull()!!

    fun getOrganizer(): Organizer = organizerRepo.findByIdOrNull(ApiRequestContextHolder.organizerId!!)
            ?: throw ZenorgerException(HttpStatus.BAD_REQUEST, "Organizer not found.")
}
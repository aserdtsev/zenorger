package ru.serdtsev.zenorger.organizer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
}
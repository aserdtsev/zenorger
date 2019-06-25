package ru.serdtsev.zenorger.organizer

import org.springframework.data.jpa.repository.JpaRepository
import ru.serdtsev.zenorger.user.User
import java.util.*

interface OrganizerRepo: JpaRepository<Organizer, UUID> {
    fun findByUser(user: User): List<Organizer>
}

interface TaskRepo: JpaRepository<Task, UUID> {
    fun findByOrganizerId(organizerId: UUID): List<Task>
}

interface TaskContextRepo: JpaRepository<TaskContext, UUID> {
    fun findByOrganizerId(organizerId: UUID): List<TaskContext>

}
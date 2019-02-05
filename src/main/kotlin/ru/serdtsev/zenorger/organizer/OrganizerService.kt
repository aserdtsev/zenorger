package ru.serdtsev.zenorger.organizer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.user.User
import java.util.*

@Service
class OrganizerService(val organizerRepo: OrganizerRepo, val taskListRepo: TaskListRepo) {
    @Transactional
    fun createOrganizer(id: UUID, user: User): Organizer {
        val organizer = Organizer(id, user, "Default organizer")
        organizerRepo.save(organizer)
        createDefaultTaskLists(organizer)
        return organizer
    }

    private fun createDefaultTaskLists(organizer: Organizer) {
        createTaskList(UUID.randomUUID(), organizer, "Inbox", TaskListType.Inbox)
        createTaskList(UUID.randomUUID(), organizer, "Active", TaskListType.Active)
        createTaskList(UUID.randomUUID(), organizer, "Pending", TaskListType.Pending)
    }

    @Transactional
    fun createTaskList(id: UUID, organizerId: UUID, name: String, type: TaskListType): TaskList {
        val organizer = organizerRepo.findById(organizerId)
                .orElseThrow { NullPointerException("Organizer $organizerId not found.") }
        return createTaskList(id, organizer, name, type)
    }

    @Transactional
    fun createTaskList(id: UUID, organizer: Organizer, name: String, type: TaskListType): TaskList {
        val taskList = TaskList(id, organizer, name, type )
        return taskListRepo.save(taskList)
    }
}
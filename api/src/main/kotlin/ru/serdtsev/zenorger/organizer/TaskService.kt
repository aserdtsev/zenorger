package ru.serdtsev.zenorger.organizer

import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import java.util.*

@Service
@Transactional(readOnly = true)
class TaskService(val taskRepo: TaskRepo, val taskContextRepo: TaskContextRepo) {
    private val log = KotlinLogging.logger {}

    fun getTask(id: UUID): Task? = taskRepo.findByIdOrNull(id)

    fun getInbox(): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return taskRepo.findByOrganizerIdAndStatus(organizerId, TaskStatus.Inbox);
    }

    fun getList(contextId: UUID?, status: TaskStatus?): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        val context = taskContextRepo.findByOrganizerIdAndId(organizerId, contextId)
        val contexts = if (context != null) listOf(context) else emptyList()
        return if (contexts.isNotEmpty() && status != null)
            taskRepo.findByOrganizerIdAndContextsAndStatus(organizerId, contexts, status)
        else if (contexts.isNotEmpty() && status == null)
            taskRepo.findByOrganizerIdAndContexts(organizerId, contexts)
        else if (contexts.isEmpty() && status != null)
            taskRepo.findByOrganizerIdAndStatus(organizerId, status)
        else
            taskRepo.findByOrganizerId(organizerId)
    }

    @Transactional(readOnly = false)
    fun createOrUpdateTask(task: Task): Task {
        task.isProject = !task.projectTasks.isNullOrEmpty()
        return taskRepo.save(task)
    }
}
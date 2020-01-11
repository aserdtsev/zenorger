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

    fun getList(code: String): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return if (TaskStatus.values().any { it.name == code }) {
            val status = TaskStatus.valueOf(code)
            taskRepo.findByOrganizerIdAndStatusOrderByCreatedAt(organizerId, status)
        } else {
            val contextId = UUID.fromString(code)
            val context = taskContextRepo.findByOrganizerIdAndId(organizerId, contextId)
            val contexts = if (context != null) listOf(context) else emptyList()
            taskRepo.findByOrganizerIdAndContextsOrderByCreatedAt(organizerId, contexts)
        }
    }

    @Transactional(readOnly = false)
    fun createOrUpdateTask(task: Task): Task {
        task.isProject = !task.projectTasks.isNullOrEmpty()
        if (task.status in listOf(TaskStatus.Inbox, TaskStatus.Active)) {
            task.status = if (task.contexts.isNullOrEmpty()) TaskStatus.Inbox else TaskStatus.Active
        } else task.contexts = null
        return taskRepo.save(task)
    }
}
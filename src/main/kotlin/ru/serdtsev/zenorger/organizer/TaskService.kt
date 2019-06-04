package ru.serdtsev.zenorger.organizer

import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import java.util.*

@Service
@Transactional(readOnly = true)
class TaskService(val taskRepo: TaskRepo) {
    private val log = KotlinLogging.logger {}

    fun getTask(id: UUID): Task? = taskRepo.findByIdOrNull(id)

    fun getList(): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return taskRepo.findByOrganizerId(organizerId)
    }

    @Transactional(readOnly = false)
    fun createOrUpdateTask(task: Task): Task {
        task.isProject = !task.projectTasks.isNullOrEmpty()
        return taskRepo.save(task)
    }
}
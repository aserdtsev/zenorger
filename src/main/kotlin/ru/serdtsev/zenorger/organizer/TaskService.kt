package ru.serdtsev.zenorger.organizer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import ru.serdtsev.zenorger.common.ZenorgerException
import java.util.*

@Service
@Transactional(readOnly = true)
class TaskService(val taskRepo: TaskRepo) {
    fun getTask(id: UUID): Task? = taskRepo.findByIdOrNull(id)

    fun getList(): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return taskRepo.findByOrganizerId(organizerId)
    }

    @Transactional(readOnly = false)
    fun createOrUpdateTask(task: Task): Task {
        task.projects?.forEach {
            if (it.isProject != true) {
                it.isProject = true
                taskRepo.save(it)
            }
        }
        return taskRepo.save(task)
    }
}
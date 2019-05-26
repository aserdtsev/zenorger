package ru.serdtsev.zenorger.organizer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
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
    fun createTask(task: Task): Task {
        return taskRepo.save(task)
    }

}
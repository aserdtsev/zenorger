package ru.serdtsev.zenorger.organizer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder

@Service
class TaskService(val taskRepo: TaskRepo) {
    fun getList(): List<Task> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return taskRepo.findByOrganizerId(organizerId)
    }

    @Transactional
    fun createTask(task: Task): Task {
        return taskRepo.save(task)
    }
}
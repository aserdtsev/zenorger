package ru.serdtsev.zenorger.organizer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(val taskRepo: TaskRepo) {
    fun list(): List<Task> {
        return taskRepo.findByOrganizerId()
    }

    @Transactional
    fun createTask(task: Task): Task {
        return taskRepo.save(task)
    }
}
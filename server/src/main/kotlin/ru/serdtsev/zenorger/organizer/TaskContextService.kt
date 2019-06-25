package ru.serdtsev.zenorger.organizer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import java.util.*

@Service
@Transactional(readOnly = true)
class TaskContextService(val taskContextRepo: TaskContextRepo) {
    fun getTaskContext(id: UUID): TaskContext? = taskContextRepo.findByIdOrNull(id)

    fun getList(): List<TaskContext> {
        val organizerId = ApiRequestContextHolder.organizerId!!
        return taskContextRepo.findByOrganizerId(organizerId)
    }

    @Transactional(readOnly = false)
    fun createOrUpdateTaskContext(taskContext: TaskContext) = taskContextRepo.save(taskContext)
}
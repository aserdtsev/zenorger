package ru.serdtsev.zenorger.organizer

import java.time.ZonedDateTime
import java.util.*

data class TaskDto(
        val id: UUID,
        val createdAt: ZonedDateTime,
        var name: String,
        var status: String? = null,
        var description: String? = null,
        var startDate: String? = null,
        var startTime: String? = null,
        var completeDate: String? = null,
        var completeTime: String? = null,
        var periodicity: PeriodicityDto? = null,
        var isProject: Boolean? = null,
        var projectTasksInOrder: Boolean? = null,
        var projectTasks: List<NamedObjectDto>? = null,
        var projects: List<NamedObjectDto>? = null,
        var contexts: List<UUID>? = null,
        var tags: List<String>? = null,
        var comments: List<UUID>? = null
)

data class NamedObjectDto(
        val id: UUID,
        val name: String
)

data class PeriodicityDto(
        val period: String,
        val periodQty: Int,
        val repeatQty: Int? = null,
        val startDate: String,
        val startTime: String? = null,
        val finishDate: String? = null
)

data class TaskContextDto(
        val id: UUID,
        val name: String,
        val tasks: List<UUID> = emptyList()
)
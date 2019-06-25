package ru.serdtsev.zenorger.organizer

import java.util.*

data class TaskDto(
        val id: UUID,
        val name: String,
        val status: String?,
        val description: String? = null,
        val startDate: String? = null,
        val startTime: String? = null,
        val completeDate: String? = null,
        val completeTime: String? = null,
        val periodicity: PeriodicityDto? = null,
        val isProject: Boolean? = null,
        val projectTasksInOrder: Boolean? = null,
        val projectTasks: List<NamedObjectDto>? = null,
        val projects: List<NamedObjectDto>? = null,
        val contexts: List<UUID>? = null,
        val tags: List<String>? = null,
        val comments: List<UUID>? = null
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
        val name: String
)
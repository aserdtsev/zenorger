package ru.serdtsev.zenorger.organizer

import org.springframework.context.ApplicationContext
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class TaskDtoToTaskConverter(appCtx: ApplicationContext) : Converter<TaskDto, Task> {
    private val organizerService = appCtx.getBean(OrganizerService::class.java)

    override fun convert(src: TaskDto): Task? {
        val organizer = organizerService.getOrganizer()
        val status = src.status?.let { TaskStatus.valueOf(src.status) } ?: TaskStatus.Inbox
        val startDate = src.startDate?.let { LocalDate.parse(it) }
        val startTime = src.startTime?.let { LocalTime.parse(it) }
        val completeDate = src.completeDate?.let { LocalDate.parse(it) }
        val completeTime = src.completeTime?.let { LocalTime.parse(it) }
        return Task(src.id, organizer, src.name, status, src.description, startDate, startTime, completeDate, completeTime)
    }
}

@Component
class TaskToTaskDtoConverter(val appCtx: ApplicationContext): Converter<Task, TaskDto> {
    override fun convert(src: Task): TaskDto? {
        val conversionService = appCtx.getBean(ConversionService::class.java)
        val periodicity = conversionService.convert(src.periodicity, PeriodicityDto::class.java)
        val contexts = src.contexts?.map { it.id }
        val tags = src.tags?.map { it.name }
        val comments = src.comments?.map { it.id }
        return TaskDto(src.id, src.name, src.status.name, src.description, src.startDate?.toString(),
                src.startTime?.toString(), src.completeDate?.toString(), src.completeTime?.toString(), periodicity,
                emptyMap(), contexts, tags, comments)
    }
}

@Component
class PeriodicityToPeriodicityDtoConverter: Converter<Periodicity, PeriodicityDto> {
    override fun convert(src: Periodicity): PeriodicityDto? {
        return PeriodicityDto(src.period.name, src.periodQty, src.repeatQty, src.startDate.toString(),
                src.startTime.toString(), src.finishDate.toString())
    }
}





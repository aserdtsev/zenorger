package ru.serdtsev.zenorger.organizer

import org.springframework.context.ApplicationContext
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.serdtsev.zenorger.common.RequestContext
import ru.serdtsev.zenorger.common.ZenorgerException
import java.time.LocalDate
import java.time.LocalTime

@Component
class TaskDtoToTaskConverter(appCtx: ApplicationContext) : Converter<TaskDto, Task> {
    private val requestContext = appCtx.getBean(RequestContext::class.java)
    private val organizerRepo = appCtx.getBean(OrganizerRepo::class.java)
    private val taskRepo = appCtx.getBean(TaskRepo::class.java)

    override fun convert(src: TaskDto): Task? {
        val organizer = organizerRepo.findByIdOrNull(requestContext.organizerId!!)
                ?: run { throw ZenorgerException(HttpStatus.BAD_REQUEST, "Organizer not found.") }
        val parentTask = src.parentId?.let {
            taskRepo.findByIdOrNull(it) ?: run { throw ZenorgerException(HttpStatus.BAD_REQUEST, "Parent task not found.") }
        }
        val status = src.status?.let { TaskStatus.valueOf(src.status) } ?: TaskStatus.Inbox
        val startDate = src.startDate?.let { LocalDate.parse(it) }
        val startTime = src.startTime?.let { LocalTime.parse(it) }
        val completeDate = src.completeDate?.let { LocalDate.parse(it) }
        val completeTime = src.completeTime?.let { LocalTime.parse(it) }
        return Task(src.id, organizer, src.name, status, src.description, startDate, startTime,
                completeDate, completeTime, null, parentTask)
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
                src.parent?.id, contexts, tags, comments)
    }
}

@Component
class PeriodicityToPeriodicityDtoConverter: Converter<Periodicity, PeriodicityDto> {
    override fun convert(src: Periodicity): PeriodicityDto? {
        return PeriodicityDto(src.period.name, src.periodQty, src.repeatQty, src.startDate.toString(),
                src.startTime.toString(), src.finishDate.toString())
    }
}





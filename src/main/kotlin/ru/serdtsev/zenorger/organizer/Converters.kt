package ru.serdtsev.zenorger.organizer

import org.springframework.context.ApplicationContext
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class TaskDtoToTaskConverter(val appCtx: ApplicationContext) : Converter<TaskDto, Task> {
    override fun convert(src: TaskDto): Task? {
        val organizerService = appCtx.getBean(OrganizerService::class.java)
        val taskService = appCtx.getBean(TaskService::class.java)
        val organizer = organizerService.getOrganizer()
        val status = src.status?.let { TaskStatus.valueOf(src.status) } ?: TaskStatus.Inbox
        val startDate = src.startDate?.let { LocalDate.parse(it) }
        val startTime = src.startTime?.let { LocalTime.parse(it) }
        val completeDate = src.completeDate?.let { LocalDate.parse(it) }
        val completeTime = src.completeTime?.let { LocalTime.parse(it) }
        val projectTasks = src.projectTasks?.map { taskService.getTask(it.id)!! }
        val projects = src.projects?.map { taskService.getTask(it.id)!! }
        return Task(src.id, organizer, src.name, status, src.description, startDate, startTime, completeDate, completeTime,
                isProject = src.isProject, projectTasksInOrder = src.projectTasksInOrder, projectTasks = projectTasks,
                projects = projects)
    }
}

@Component
class TaskToTaskDtoConverter(val appCtx: ApplicationContext): Converter<Task, TaskDto> {
    override fun convert(src: Task): TaskDto? {
        val conversionService = appCtx.getBean(ConversionService::class.java)
        val periodicity = conversionService.convert(src.periodicity, PeriodicityDto::class.java)
        val projectTasks = src.projectTasks?.map { NamedObjectDto(it.id, it.name) }
        val projects = src.projects?.map { NamedObjectDto(it.id, it.name) }
        val contexts = src.contexts?.map { it.id }
        val tags = src.tags?.map { it.name }
        val comments = src.comments?.map { it.id }
        return TaskDto(src.id, src.name, src.status.name, src.description, src.startDate?.toString(),
                src.startTime?.toString(), src.completeDate?.toString(), src.completeTime?.toString(), periodicity,
                src.isProject, src.projectTasksInOrder, projectTasks, projects, contexts, tags, comments)
    }
}

@Component
class PeriodicityToPeriodicityDtoConverter: Converter<Periodicity, PeriodicityDto> {
    override fun convert(src: Periodicity): PeriodicityDto? {
        return PeriodicityDto(src.period.name, src.periodQty, src.repeatQty, src.startDate.toString(),
                src.startTime.toString(), src.finishDate.toString())
    }
}





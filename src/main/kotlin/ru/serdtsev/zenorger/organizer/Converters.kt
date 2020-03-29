package ru.serdtsev.zenorger.organizer

import org.springframework.context.ApplicationContext
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class TaskDtoToTaskConverter(val appCtx: ApplicationContext) : Converter<TaskDto, Task> {
    override fun convert(src: TaskDto): Task? {
        val organizerService = appCtx.getBean(OrganizerService::class.java)
        val taskService = appCtx.getBean(TaskService::class.java)
        val taskContextService = appCtx.getBean(TaskContextService::class.java)
        val organizer = organizerService.getOrganizer()
        val status = src.status?.let { TaskStatus.valueOf(src.status!!) } ?: TaskStatus.Inbox
        val startDate = src.startDate?.let { LocalDate.parse(it) }
        val startTime = src.startTime?.let { LocalTime.parse(it) }
        val completeDate = src.completeDate?.let { LocalDate.parse(it) }
        val completeTime = src.completeTime?.let { LocalTime.parse(it) }
        val projectTasks = src.projectTasks?.map { taskService.getTask(it.id)!! }?.toMutableList()
        val projects = src.projects?.map { taskService.getTask(it.id)!! }?.toMutableList()
        val contexts = src.contexts?.map { taskContextService.getTaskContext(it)!! }?.toMutableList()
        return Task(src.id, organizer, src.createdAt, src.name, status, src.description, startDate, startTime, completeDate, completeTime,
                isProject = src.isProject, projectTasksInOrder = src.projectTasksInOrder, projectTasks = projectTasks,
                projects = projects, contexts = contexts)
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
        return TaskDto(src.id, src.createdAt, src.name, src.status.name, src.description, src.startDate?.toString(),
                src.startTime?.toString(), src.completeDate?.toString(), src.completeTime?.toString(), periodicity,
                src.isProject, src.projectTasksInOrder, projectTasks, projects, contexts, tags, comments)
    }
}

@Component
class PeriodicityToPeriodicityDtoConverter: Converter<Periodicity, PeriodicityDto> {
    override fun convert(src: Periodicity): PeriodicityDto? =
            PeriodicityDto(src.period.name, src.periodQty, src.repeatQty, src.startDate.toString(),
                    src.startTime.toString(), src.finishDate.toString())
}

@Component
class TaskContextToTaskContextDtoConverter: Converter<TaskContext, TaskContextDto> {
    override fun convert(src: TaskContext): TaskContextDto? {
        val tasks = src.tasks.map { it.id }
        return TaskContextDto(src.id, src.name, tasks)
    }
}

@Component
class TaskContextDtoToTaskContextConverter(val appCtx: ApplicationContext): Converter<TaskContextDto, TaskContext> {
    override fun convert(src: TaskContextDto): TaskContext? {
        val organizerService = appCtx.getBean(OrganizerService::class.java)
        val organizer = organizerService.getOrganizer()
        val taskRepo = appCtx.getBean(TaskRepo::class.java)
        val tasks = src.tasks.map { taskRepo.findByIdOrNull(it)!! }.toMutableList()
        return TaskContext(src.id, organizer, src.name, tasks)
    }
}



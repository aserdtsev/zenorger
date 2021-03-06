package ru.serdtsev.zenorger.organizer

import org.springframework.core.convert.ConversionService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/task"])
class TaskController(val taskService: TaskService, val conversionService: ConversionService) {
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET], produces = [APPLICATION_JSON_VALUE])
    fun list(@RequestParam(required = false) code: String): List<TaskDto> {
        val tasks = taskService.getList(code)
        return tasks.map { conversionService.convert(it, TaskDto::class.java)!! }
    }

    @RequestMapping(value = ["/add", "/update"], method = [RequestMethod.POST], consumes = [APPLICATION_JSON_VALUE],
            produces = [APPLICATION_JSON_VALUE])
    fun addOrUpdateTask(@RequestBody taskDto: TaskDto): TaskDto {
        var task = conversionService.convert(taskDto, Task::class.java)!!
        task = taskService.createOrUpdateTask(task)
        return conversionService.convert(task, TaskDto::class.java)!!
    }
}
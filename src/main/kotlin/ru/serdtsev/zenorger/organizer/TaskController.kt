package ru.serdtsev.zenorger.organizer

import mu.KotlinLogging
import org.springframework.core.convert.ConversionService
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/task"])
class TaskController(val taskService: TaskService, val conversionService: ConversionService) {
    private val log = KotlinLogging.logger {}

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST], consumes = [APPLICATION_JSON_UTF8_VALUE],
            produces = [APPLICATION_JSON_UTF8_VALUE])
    fun addTask(@RequestBody taskDto: TaskDto): TaskDto {
        var task = conversionService.convert(taskDto, Task::class.java)!!
        task = taskService.createTask(task)
        return conversionService.convert(task, TaskDto::class.java)!!
    }

}
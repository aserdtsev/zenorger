package ru.serdtsev.zenorger.organizer

import org.springframework.core.convert.ConversionService
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/task"])
class TaskController(val taskService: TaskService, val conversionService: ConversionService) {
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun list(): List<TaskDto> {
        checkAppRequestContext()
        val tasks = taskService.getList()
        return tasks.map { conversionService.convert(it, TaskDto::class.java)!! }
    }

    @RequestMapping(value = ["/add", "/update"], method = [RequestMethod.POST], consumes = [APPLICATION_JSON_UTF8_VALUE],
            produces = [APPLICATION_JSON_UTF8_VALUE])
    fun addOrUpdateTask(@RequestBody taskDto: TaskDto): TaskDto {
        checkAppRequestContext()
        var task = conversionService.convert(taskDto, Task::class.java)!!
        task = taskService.createOrUpdateTask(task)
        return conversionService.convert(task, TaskDto::class.java)!!
    }

}
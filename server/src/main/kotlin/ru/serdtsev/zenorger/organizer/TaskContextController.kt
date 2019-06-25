package ru.serdtsev.zenorger.organizer

import org.springframework.core.convert.ConversionService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/context"])
class TaskContextController(val taskContextService: TaskContextService, val conversionService: ConversionService) {
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun list(): List<TaskContextDto> {
        val taskContexts = taskContextService.getList()
        return taskContexts.map { conversionService.convert(it, TaskContextDto::class.java)!! }
    }

    @RequestMapping(value = ["/add", "/update"], method = [RequestMethod.POST], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE],
            produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun addOrUpdateTaskContext(@RequestBody taskContextDto: TaskContextDto): TaskContextDto {
        var taskContext = conversionService.convert(taskContextDto, TaskContext::class.java)!!
        taskContext = taskContextService.createOrUpdateTaskContext(taskContext)
        return conversionService.convert(taskContext, TaskContextDto::class.java)!!
    }

}
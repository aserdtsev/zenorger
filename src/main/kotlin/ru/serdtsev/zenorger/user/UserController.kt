package ru.serdtsev.zenorger.user

import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.serdtsev.zenorger.organizer.OrganizerService

@RestController
@RequestMapping(value = ["/user"])
class UserController(val userService: UserService, val organizerService: OrganizerService) {
    private val log = KotlinLogging.logger {}

    @RequestMapping(value = ["/auth"], method = [RequestMethod.POST])
    fun auth(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String) {
        val user = userService.getUser(authorization)
        val organizer = organizerService.getDefaultOrganizerByUser(user)
        MDC.put("Organizer-Id", organizer.id.toString())
        log.debug { "User ${user.login} login." }
    }

    @RequestMapping(value = ["/signup"], method = [RequestMethod.POST])
    fun signup(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String) {
        userService.signup(authorization)
    }

}
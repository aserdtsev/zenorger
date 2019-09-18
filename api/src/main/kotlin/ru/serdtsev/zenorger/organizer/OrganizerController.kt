package ru.serdtsev.zenorger.organizer

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.serdtsev.zenorger.user.UserService
import java.util.*

@RestController
@RequestMapping(value = ["/api/organizer"])
class OrganizerController(
        val organizerService: OrganizerService,
        val userService: UserService
) {
    @RequestMapping(value = ["/getDefaultOrganizerId"], method = [RequestMethod.GET])
    fun getDefaultOrganizerId(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String): UUID {
        val user = userService.getUser(authorization)
        val organizer = organizerService.getDefaultOrganizerByUser(user)
        return organizer.id
    }
}
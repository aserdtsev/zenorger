package ru.serdtsev.zenorger.user

import mu.KotlinLogging
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/user"])
class UserController(val userService: UserService) {
    private val log = KotlinLogging.logger {}

    @RequestMapping(value = ["/auth"], method = [RequestMethod.POST])
    fun auth(@RequestHeader("Authorization") authorization: String) {
        val user = userService.getUser(authorization)
        log.debug { "User ${user.login} login." }
    }

    @RequestMapping(value = ["/signup"], method = [RequestMethod.POST])
    fun signup(@RequestHeader("Authorization") authorization: String) {
        userService.signup(authorization)
    }

}
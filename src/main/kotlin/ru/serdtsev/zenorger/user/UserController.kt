package ru.serdtsev.zenorger.user

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/user"])
class UserController(val userService: UserService) {
    private val log = KotlinLogging.logger {}

    @RequestMapping(value = ["/auth"], method = [RequestMethod.POST])
    fun auth(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String) {
        val user = userService.getUser(authorization)
        log.debug { "User ${user.login} has logged in." }
    }

    @RequestMapping(value = ["/signUp"], method = [RequestMethod.POST])
    fun signUp(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String) {
        userService.signUp(authorization)
    }

}
package ru.serdtsev.zenorger.user

import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/user"])
class UserController(val userService: UserService) {

    @RequestMapping(value = ["/auth"], method = [RequestMethod.POST])
    fun auth(@RequestHeader("Authorization") authorization: String) {
        val user = userService.getUser(authorization)
        println(user)
    }

    @RequestMapping(value = ["/signup"], method = [RequestMethod.POST])
    fun signup(@RequestHeader("Authorization") authorization: String) {
        val user = userService.signup(authorization)
        println(user)
    }

}
package ru.serdtsev.zenorger.user

import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/user"])
class UserController(val userService: UserService/*, val ignite: Ignite*/) {

    @RequestMapping("/auth")
    fun auth(@RequestHeader("Authorization") authorization: String) {
        val user = userService.getUser(authorization)
        println(user)
    }

    @RequestMapping("/add")
    fun addUser(@RequestHeader("Authorization") authorization: String) {
        val user = userService.getUser(authorization)
        println(user)
    }

}
package ru.serdtsev.zenorger

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping
class AppController {
    @ApiIgnore
    @RequestMapping("/")    // чтобы каждый раз не вспоминать адрес документации
    fun index(): ModelAndView = ModelAndView("redirect:swagger-ui.html")
}

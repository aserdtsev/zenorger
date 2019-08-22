package ru.serdtsev.zenorger.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZenorgerWebApplication

fun main(args: Array<String>) {
    runApplication<ZenorgerWebApplication>(*args)
}

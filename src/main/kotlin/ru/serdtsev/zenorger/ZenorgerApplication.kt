package ru.serdtsev.zenorger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZenorgerApplication

fun main(args: Array<String>) {
    runApplication<ZenorgerApplication>(*args)
}

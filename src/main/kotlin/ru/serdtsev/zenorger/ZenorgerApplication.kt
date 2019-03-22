package ru.serdtsev.zenorger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class ZenorgerApplication

fun main(args: Array<String>) {
    runApplication<ZenorgerApplication>(*args)
}

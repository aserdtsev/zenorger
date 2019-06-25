package ru.serdtsev.zenorger.common

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@ControllerAdvice
@RestController
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger {  }

    @ExceptionHandler(ZenorgerException::class)
    fun onZenorgerException(ex: ZenorgerException, request: WebRequest): ResponseEntity<Any> {
        val path = (request as ServletWebRequest).request.requestURI
        val response = HttpErrorResponse(Instant.now(), ex.httpStatus.value(), ex.error, ex.message, path)
        log.info { response }
        return handleExceptionInternal(ex, response, HttpHeaders.EMPTY, ex.httpStatus, request)
    }
}
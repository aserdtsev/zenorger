package ru.serdtsev.zenorger.common

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
@RestController
class GlobalExceptionHandler {
    @ExceptionHandler(AuthException::class)
    fun onAuthException(response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
    }
}
package ru.serdtsev.zenorger.common

import org.springframework.http.HttpStatus
import java.time.Instant

open class ZenorgerException(val httpStatus: HttpStatus, val error: String, message: String? = null): RuntimeException(message)

class LoginExistsException(message: String): ZenorgerException(HttpStatus.FORBIDDEN, "Login exists", message)

data class HttpErrorResponse(
        val timestamp: Instant,
        val status: Int,
        val error: String,
        val message: String?,
        val path: String?
)
package ru.serdtsev.zenorger.common

import org.springframework.stereotype.Component
import java.util.*
import kotlin.concurrent.getOrSet

@Component
class ApiRequestContextHolder {
    companion object {
        private val requestContextTls = ThreadLocal<ApiRequestContext>()
        var apiRequestContext: ApiRequestContext
            get() = requestContextTls.getOrSet { ApiRequestContext() }
            set(value) = requestContextTls.set(value)

        var requestId: String?
            get() = apiRequestContext.requestId
            set(value) {
                apiRequestContext.requestId = value
            }

        var organizerId: UUID?
            get() = apiRequestContext.organizerId
            set(value) {
                apiRequestContext.organizerId = value
            }

        fun clear() {
            requestContextTls.remove()
        }
    }
}

data class ApiRequestContext(
    var requestId: String? = null,
    var organizerId: UUID? = null
)
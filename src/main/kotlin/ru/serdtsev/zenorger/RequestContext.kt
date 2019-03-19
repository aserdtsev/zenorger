package ru.serdtsev.zenorger

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@Component
@RequestScope
data class RequestContext(
    var requestId: String? = null,
    var organizerId: UUID? = null
)
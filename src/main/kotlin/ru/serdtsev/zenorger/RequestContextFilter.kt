package ru.serdtsev.zenorger

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order
class RequestContextFilter: Filter {

}
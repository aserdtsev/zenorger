package ru.serdtsev.zenorger.organizer

import ru.serdtsev.zenorger.common.ApiRequestContextHolder

fun checkAppRequestContext() {
    assert(ApiRequestContextHolder.organizerId != null) { "Header 'X-Organizer-Id' is not defined." }
}

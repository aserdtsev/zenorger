package ru.serdtsev.zenorger.common

import org.apache.ignite.cache.query.annotations.QuerySqlField
import java.io.Serializable
import java.util.*

data class Realm(
    @QuerySqlField(index = true) val id: UUID
): Serializable
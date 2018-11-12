package ru.serdtsev.zenorger.user

import org.apache.ignite.cache.query.annotations.QuerySqlField
import ru.serdtsev.zenorger.common.Realm
import java.io.Serializable
import java.util.*

data class User(
    @QuerySqlField val id: UUID,
    @QuerySqlField val login: String,
    @QuerySqlField val password: String,
    @QuerySqlField(name = "realm_id") val realmId: UUID
): Serializable

package ru.serdtsev.zenorger.user

import org.apache.ignite.Ignite
import org.apache.ignite.cache.CacheAtomicityMode
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cache.CacheWriteSynchronizationMode
import org.apache.ignite.configuration.CacheConfiguration
import org.springframework.stereotype.Service
import ru.serdtsev.zenorger.common.Realm
import java.util.*

@Service
class UserService(val ignite: Ignite) {
    @Suppress("unused")
    fun auth(authorization: String): Boolean = authorization == "Basic YW5kcmV5LnNlcmR0c2V2QGdtYWlsLmNvbToxMjM0NTY="

    fun getUser(authorization: String): User {
        val base64Credentials = authorization.substring("Basic".length).trim()
        val credDecoded = Base64.getDecoder().decode(base64Credentials)
        val credentials = String(credDecoded)
        val (login, password) = credentials.split(":".toRegex(), 2).toTypedArray()

        val userCache = ignite.getOrCreateCache<UUID, User>("user")
        val user = userCache.find { it.value.login == login }?.value ?: run {
            val realm = Realm(UUID.randomUUID())
            val realmCache = ignite.getOrCreateCache<Realm, UUID>("realm")
            realmCache.put(realm, realm.id)
            User(UUID.randomUUID(), login, password, realm.id)
        }

        userCache.putIfAbsent(user.id, user)

        return user
    }
}

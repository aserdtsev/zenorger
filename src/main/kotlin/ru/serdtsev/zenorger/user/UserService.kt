package ru.serdtsev.zenorger.user

import mu.KotlinLogging
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.LoginExistsException
import ru.serdtsev.zenorger.common.Organizer
import ru.serdtsev.zenorger.common.OrganizerRepo
import java.time.OffsetDateTime
import java.util.*

@Service
class UserService(val encoder: BCryptPasswordEncoder, val userRepo: UserRepo, val organizerRepo: OrganizerRepo) {
    private val log = KotlinLogging.logger {  }
    
    fun getUser(authorization: String): User {
        val login = decodeAuthorization(authorization).first
        return userRepo.findByLogin(login)!!
    }

    @Transactional
    fun signup(authorization: String): User {
        val (login, password) = decodeAuthorization(authorization)
        log.info { "Login $login." }
        
        userRepo.findByLogin(login)?.also { throw LoginExistsException(login) }
        val user = User(UUID.randomUUID(), OffsetDateTime.now(), login, encoder.encode(password))
        val organizer = Organizer(UUID.randomUUID(), OffsetDateTime.now(), user,"Default organizer")
        userRepo.save(user)
        organizerRepo.save(organizer)
        return user
    }

    private fun decodeAuthorization(authorization: String): Pair<String, String> {
        val base64Credentials = authorization.substring("Basic".length).trim()
        val credDecoded = Base64.getDecoder().decode(base64Credentials)
        val credentials = String(credDecoded)
        val arrayCredential = credentials.split(":".toRegex(), 2)
        return Pair(arrayCredential[0], arrayCredential[1])
    }
}

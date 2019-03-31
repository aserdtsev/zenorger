package ru.serdtsev.zenorger.user

import mu.KotlinLogging
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.serdtsev.zenorger.common.LoginExistsException
import ru.serdtsev.zenorger.organizer.OrganizerService
import java.util.*
import java.util.concurrent.Future

@Service
class UserService(
        val encoder: BCryptPasswordEncoder,
        val userRepo: UserRepo,
        val organizerService: OrganizerService) {
    private val log = KotlinLogging.logger {  }

    fun getUser(authorization: String): Future<User> {
        val login = decodeAuthorization(authorization).first
        return AsyncResult(userRepo.findByLogin(login)!!)
    }

    fun createUser(login: String, password: String): User {
        userRepo.findByLogin(login)?.also { throw LoginExistsException(login) }
        val user = User(UUID.randomUUID(), login, encoder.encode(password))
        userRepo.save(user)
        return user
    }

    @Transactional
    fun signUp(authorization: String): User {
        val (login, password) = decodeAuthorization(authorization)
        log.info { "Login $login." }
        val user = createUser(login, password)
        organizerService.createOrganizer(UUID.randomUUID(), user)
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

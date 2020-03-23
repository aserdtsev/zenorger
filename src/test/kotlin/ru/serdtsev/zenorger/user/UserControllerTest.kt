package ru.serdtsev.zenorger.user

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import junit.framework.TestCase.assertEquals
import org.flywaydb.test.annotation.FlywayTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import ru.serdtsev.zenorger.common.LoginExistsException
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
class UserControllerTest {
    @Autowired
    lateinit var userController: UserController

    @Test
    fun `signUp && auth`() {
        val credentials = "user@gmail.com:123456"
        val authorization= "Basic" + String(Base64.getEncoder().encode(credentials.toByteArray()))
        userController.signUp(authorization)
        assertThrows(LoginExistsException::class.java) { userController.signUp(authorization) }
        userController.auth(authorization)
    }
}
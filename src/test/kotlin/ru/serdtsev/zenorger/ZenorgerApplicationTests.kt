package ru.serdtsev.zenorger

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import ru.serdtsev.zenorger.common.LoginExistsException
import ru.serdtsev.zenorger.organizer.OrganizerController
import ru.serdtsev.zenorger.organizer.TaskContextController
import ru.serdtsev.zenorger.user.UserController
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
class ZenorgerApplicationTests {
	val organizerId = UUID.fromString("640021fc-4093-4dd4-84f2-5792a6116cb7")!!

	@Autowired lateinit var userController: UserController
	@Autowired lateinit var organizerController: OrganizerController
	@Autowired lateinit var taskContextController: TaskContextController

	@Before
	fun setUp() {
		ApiRequestContextHolder.organizerId = organizerId
	}

	@Test
	fun `signUp && auth`() {
		val authorization = getAuthorization("user@gmail.com","123456")
		userController.signUp(authorization)
		assertThrows(LoginExistsException::class.java) { userController.signUp(authorization) }
		userController.auth(authorization)
	}

	@Test
	fun getDefaultOrganizerId() {
		val authorization = getAuthorization("andrey.serdtsev@gmail.com","123456")
		assertEquals(organizerId, organizerController.getDefaultOrganizerId(authorization))
	}

	@Test
	fun `api context list`() {
		taskContextController.list()
	}

	@Suppress("SameParameterValue")
	private fun getAuthorization(user: String, password: String): String {
		val credentials = "$user:$password"
		return "Basic" + String(Base64.getEncoder().encode(credentials.toByteArray()))
	}
}

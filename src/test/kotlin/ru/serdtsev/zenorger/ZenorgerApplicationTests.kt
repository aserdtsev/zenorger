package ru.serdtsev.zenorger

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import ru.serdtsev.zenorger.common.LoginExistsException
import ru.serdtsev.zenorger.organizer.OrganizerController
import ru.serdtsev.zenorger.organizer.TaskContextController
import ru.serdtsev.zenorger.organizer.TaskContextDto
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
		ApiRequestContextHolder.organizerId = organizerId
		val authorization = getAuthorization("andrey.serdtsev@gmail.com","123456")
		assertEquals(organizerId, organizerController.getDefaultOrganizerId(authorization))
	}

	@Test
	fun `TaskContext management`() {
		ApiRequestContextHolder.organizerId = createNewOrganizerAndGetId()
		var dto = TaskContextDto(UUID.randomUUID(), "Shop", emptyList())
		taskContextController.addOrUpdateTaskContext(dto)
		assertTrue(taskContextController.list().any { it == dto })

		dto = TaskContextDto(dto.id, "Work", dto.tasks)
		taskContextController.addOrUpdateTaskContext(dto)
		assertTrue(taskContextController.list().any { it == dto })
	}

	private fun createNewOrganizerAndGetId(): UUID {
		val user = UUID.randomUUID().toString()
		val authorization = getAuthorization(user,"123456")
		userController.signUp(authorization)
		return organizerController.getDefaultOrganizerId(authorization)
	}

	@Suppress("SameParameterValue")
	private fun getAuthorization(user: String, password: String): String {
		val credentials = "$user:$password"
		return "Basic" + String(Base64.getEncoder().encode(credentials.toByteArray()))
	}
}

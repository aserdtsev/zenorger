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
import ru.serdtsev.zenorger.organizer.*
import ru.serdtsev.zenorger.user.UserController
import java.time.ZonedDateTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
class ZenorgerApplicationTests {
	companion object {
		const val homeContextName = "Home"
		const val shopContextName = "Shop"
		val organizerId = UUID.fromString("640021fc-4093-4dd4-84f2-5792a6116cb7")!!
	}

	@Autowired lateinit var userController: UserController
	@Autowired lateinit var organizerController: OrganizerController
	@Autowired lateinit var organizerService: OrganizerService
	@Autowired lateinit var taskContextController: TaskContextController
	@Autowired lateinit var taskContextRepo: TaskContextRepo
	@Autowired lateinit var taskController: TaskController

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
		createNewOrganizerAndSetItAsDefault()
		val taskContextDto = TaskContextDto(UUID.randomUUID(), "Shop")
		taskContextController.addOrUpdateTaskContext(taskContextDto)
		assertTrue(taskContextController.list().any { it == taskContextDto })

		taskContextDto.name = "Work"
		taskContextController.addOrUpdateTaskContext(taskContextDto)
		assertTrue(taskContextController.list().any { it == taskContextDto })

		val taskDto1 = createTaskDto(contexts = listOf(taskContextDto.id))
		taskContextDto.tasks.add(taskDto1.id)
		taskController.addOrUpdateTask(taskDto1)
		val taskDto2 = createTaskDto(contexts = listOf(taskContextDto.id))
		taskContextDto.tasks.add(taskDto2.id)
		taskController.addOrUpdateTask(taskDto2)
		taskContextController.addOrUpdateTaskContext(taskContextDto)
		taskContextDto.tasks.reverse()
		taskContextController.addOrUpdateTaskContext(taskContextDto)
		val taskContext = taskContextController.list().first { it.id == taskContextDto.id }
		assertEquals(mutableListOf(taskDto2.id, taskDto1.id), taskContext.tasks)
	}

	@Test
	fun `Task management`() {
		createNewOrganizerAndSetItAsDefault(listOf(homeContextName, shopContextName))
		val organizer = organizerService.getOrganizer()
		val homeTaskContext = taskContextRepo.findByNameAndOrganizer(homeContextName, organizer)!!
		var taskDto = createTaskDto("Buy bread")
		taskDto = taskController.addOrUpdateTask(taskDto)
		assertEquals(1, taskController.list(TaskStatus.Inbox.toString()).count())

		taskDto.contexts = listOf(homeTaskContext.id)
		taskController.addOrUpdateTask(taskDto)
		assertEquals(0, taskController.list(TaskStatus.Inbox.toString()).count())
		assertEquals(1, taskController.list(homeTaskContext.id.toString()).count())
	}

	private fun createTaskDto(name: String? = null, contexts: List<UUID> = emptyList()): TaskDto {
		val id = UUID.randomUUID()
		return TaskDto(id, ZonedDateTime.now(), name ?: id.toString(), contexts = contexts)
	}

	private fun createNewOrganizerAndSetItAsDefault(contextNames: List<String> = emptyList()) {
		val user = UUID.randomUUID().toString()
		val authorization = getAuthorization(user,"123456")
		userController.signUp(authorization)
		ApiRequestContextHolder.organizerId = organizerController.getDefaultOrganizerId(authorization)
		contextNames.forEach {
			val dto = TaskContextDto(UUID.randomUUID(), it)
			taskContextController.addOrUpdateTaskContext(dto)
		}
	}

	@Suppress("SameParameterValue")
	private fun getAuthorization(user: String, password: String): String {
		val credentials = "$user:$password"
		return "Basic" + String(Base64.getEncoder().encode(credentials.toByteArray()))
	}
}

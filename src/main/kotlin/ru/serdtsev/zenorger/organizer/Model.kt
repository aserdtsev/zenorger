package ru.serdtsev.zenorger.organizer

import ru.serdtsev.zenorger.user.User
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "organizer")
data class Organizer (
        @Id val id: UUID? = null,
        @ManyToOne val user: User? = null,
        var name: String? = null
)

@Entity
@Table(name = "task_list")
data class TaskList (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        var name: String? = null,
        @Enumerated(EnumType.STRING) var type: TaskListType? = null
)

enum class TaskListType { Active, Pending, Inbox }

@Entity
@Table(name = "task")
data class Task (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        var name: String? = null,
        @Enumerated(EnumType.STRING) var status: TaskStatus? = null,
        var description: String? = null,
        var startDate: LocalDate? = null,
        var startTime: LocalTime? = null,
        var completeDate: LocalDate? = null,
        var completeTime: LocalTime? = null,
        @OneToOne val periodicity: Periodicity? = null,
        @ManyToOne var parent: Task? = null,
        @OneToMany val subTasks: List<Task>? = emptyList(),
        @ManyToMany @JoinTable(name = "task_context") val contexts: List<TaskContext>? = emptyList(),
        @ManyToMany @JoinTable(name = "task_tag") val tags: List<Tag>? = emptyList(),
        @OneToMany val comments: List<Comment>? = emptyList()
)

enum class TaskStatus { Active, Completed }

@Entity
@Table(name = "periodicity")
data class Periodicity (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        @Enumerated(EnumType.STRING) var period: ChronoUnit? = null,
        /** Кол-во периодов в повторе */
        var periodQty: Int? = null,
        /** Кол-во повторений */
        var repeatQty: Int? = null,
        var startDate: LocalDate? = null,
        var finishDate: LocalDate? = null,
        var nextDate: LocalDate? = null
)

@Entity
@Table(name = "context")
data class TaskContext (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        var name: String? = null
)

@Entity
@Table(name = "tag")
data class Tag (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        var name: String? = null
)

@Entity
@Table(name = "comment")
data class Comment (
        @Id val id: UUID? = null,
        @ManyToOne val organizer: Organizer? = null,
        @ManyToOne val task: Task? = null,
        val createdAt: ZonedDateTime? = null,
        var text: String? = null
)
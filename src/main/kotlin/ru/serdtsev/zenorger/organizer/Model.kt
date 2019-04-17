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
        @Id val id: UUID,
        @ManyToOne val user: User,
        var name: String? = null
)

@Entity
@Table(name = "task")
data class Task (
        @Id val id: UUID,
        @ManyToOne val organizer: Organizer,
        var name: String,
        @Enumerated(EnumType.STRING) var status: TaskStatus,
        var description: String? = null,
        var startDate: LocalDate? = null,
        var startTime: LocalTime? = null,
        var completeDate: LocalDate? = null,
        var completeTime: LocalTime? = null,
        @OneToOne val periodicity: Periodicity? = null,
        @ManyToOne var parent: Task? = null,
        @OneToMany val subTasks: List<Task>? = null,
        @ManyToMany @JoinTable(name = "task_context") val contexts: List<TaskContext>? = null,
        @ManyToMany @JoinTable(name = "task_tag") val tags: List<Tag>? = null,
        @OneToMany val comments: List<Comment>? = null
)

enum class TaskStatus { Inbox, Active, Pending, SomedayMaybe, Done, Removed }

@Entity
@Table(name = "periodicity")
data class Periodicity (
        @Id val id: UUID,
        @ManyToOne val organizer: Organizer,
        @Enumerated(EnumType.STRING) var period: ChronoUnit,
        /** Кол-во периодов в повторе */
        var periodQty: Int,
        /** Кол-во повторений */
        var repeatQty: Int? = null,
        var startDate: LocalDate,
        var startTime: LocalTime? = null,
        var finishDate: LocalDate? = null,
        var nextDate: LocalDate? = null
)

@Entity
@Table(name = "context")
data class TaskContext (
        @Id val id: UUID,
        @ManyToOne val organizer: Organizer,
        var name: String
)

@Entity
@Table(name = "tag")
data class Tag (
        @Id val id: UUID,
        @ManyToOne val organizer: Organizer,
        var name: String
)

@Entity
@Table(name = "comment")
data class Comment (
        @Id val id: UUID,
        @ManyToOne val organizer: Organizer,
        @ManyToOne val task: Task,
        val createdAt: ZonedDateTime? = null,
        var text: String
)
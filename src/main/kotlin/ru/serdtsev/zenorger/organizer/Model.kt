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
        val createdAt: ZonedDateTime,
        var name: String,
        @Enumerated(EnumType.STRING) var status: TaskStatus,
        var description: String? = null,
        var startDate: LocalDate? = null,
        var startTime: LocalTime? = null,
        var completeDate: LocalDate? = null,
        var completeTime: LocalTime? = null,
        @OneToOne val periodicity: Periodicity? = null,
        var isProject: Boolean? = null,
        var projectTasksInOrder: Boolean? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "project_task",
                joinColumns = [JoinColumn(name = "project_id")],
                inverseJoinColumns = [JoinColumn(name = "task_id")])
        @OrderColumn(name = "index", nullable = false)
        var projectTasks: MutableList<Task>? = null,

        @ManyToMany(mappedBy = "projectTasks", fetch = FetchType.EAGER)
        val projects: MutableList<Task>? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "task_context",
                joinColumns = [JoinColumn(name = "task_id")],
                inverseJoinColumns = [JoinColumn(name = "context_id")])
        @OrderColumn(name = "index", nullable = false)
        var contexts: MutableList<TaskContext>? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "task_tag",
                joinColumns = [JoinColumn(name = "task_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")])
        @OrderColumn(name = "index", nullable = false)
        val tags: MutableList<Tag>? = null,

        @OneToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "comment",
                inverseJoinColumns = [JoinColumn(name = "id")])
        val comments: MutableList<Comment>? = null
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
class TaskContext (
        @Id val id: UUID,

        @ManyToOne val organizer: Organizer,
        var name: String,

        @OneToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "context_task",
                joinColumns = [JoinColumn(name = "context_id")],
                inverseJoinColumns = [JoinColumn(name = "task_id")])
        @OrderColumn(name = "index", nullable = false)
        var tasks: MutableList<Task>
) {
        override fun toString(): String {
                return "TaskContext(id=$id, organizer=$organizer, name='$name')"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is TaskContext) return false

                if (id != other.id) return false

                return true
        }

        override fun hashCode(): Int {
                return id.hashCode()
        }
}

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
        var content: String
)
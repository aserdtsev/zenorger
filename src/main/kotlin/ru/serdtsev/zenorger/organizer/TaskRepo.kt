package ru.serdtsev.zenorger.organizer

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TaskRepo: JpaRepository<Task, UUID>
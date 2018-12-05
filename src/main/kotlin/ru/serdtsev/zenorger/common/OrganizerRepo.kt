package ru.serdtsev.zenorger.common

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrganizerRepo: JpaRepository<Organizer, UUID>
package io.musicorum.api.realms.collages.schemas

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

@Serializable
data class Collage(
    var id: String,
    var file: String,
    var duration: Long,
    var createdAt: Instant?
)
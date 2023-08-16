package io.musicorum.api.realms.collages.repositories

import io.musicorum.api.realms.collages.schemas.Collage
import io.musicorum.api.services.database
import io.musicorum.api.services.runQuery
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

class CollagesRepository {
    object Collages : Table("collages") {
        val id = varchar("id", 32)
        val file = varchar("file", 42)
        val duration = long("duration")
        var createdAt = timestamp("created_at").clientDefault { java.time.Instant.now() }
    }

    private fun ResultRow.toDTO() = Collage(
        this[Collages.id],
        this[Collages.file],
        this[Collages.duration],
        this[Collages.createdAt].toKotlinInstant()
    )

    init {
        transaction(database) {
            SchemaUtils.create(Collages)
        }
    }

    suspend fun createOne(id: String, file: String, duration: Long) = runQuery{
        return@runQuery Collages.insert {
            it[Collages.id] = id
            it[Collages.file] = file
            it[Collages.duration] = duration
        }
    }

    suspend fun listAll() = runQuery {
        return@runQuery Collages.selectAll().map { it.toDTO() }
    }
}
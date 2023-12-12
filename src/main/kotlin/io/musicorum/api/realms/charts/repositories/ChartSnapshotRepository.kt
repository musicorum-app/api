package io.musicorum.api.realms.charts.repositories

import io.musicorum.api.realms.charts.schemas.ChartSnapshot
import io.musicorum.api.services.database
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.ZoneOffset

class ChartSnapshotRepository {
    object ChartSnapshot : Table("chart_snapshots") {
        val id = integer("id").autoIncrement()
        val updatedAt = datetime("updated_at")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(ChartSnapshot)
        }
    }

    suspend fun createSnapshot(): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            return@newSuspendedTransaction ChartSnapshot.insert {
                it[updatedAt] = CurrentDateTime
            }[ChartSnapshot.id]
        }
    }

    suspend fun getSnapshot(id: Int): io.musicorum.api.realms.charts.schemas.ChartSnapshot {
        return newSuspendedTransaction {
            return@newSuspendedTransaction ChartSnapshot.select { ChartSnapshot.id eq id }.map {
                ChartSnapshot(
                    it[ChartSnapshot.id],
                    it[ChartSnapshot.updatedAt].toEpochSecond(ZoneOffset.UTC)
                )
            }.first()
        }
    }
}
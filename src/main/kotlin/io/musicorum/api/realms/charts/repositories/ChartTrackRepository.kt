package io.musicorum.api.realms.charts.repositories

import io.musicorum.api.realms.charts.schemas.ChartTrack
import io.musicorum.api.services.database
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ChartTrackRepository {
    object ChartTracks : Table("chart_tracks") {
        val id = integer("id").autoIncrement()
        val name = text("name")
        val playCount = long("play_count")
        val snapshotId = integer("snapshot_id").references(ChartSnapshotRepository.ChartSnapshot.id)
        val listeners = long("listeners")
        val artist = text("artist")
        val position = integer("positions")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(ChartTracks)
        }
    }

    suspend fun insert(track: ChartTrack): Int {
        return newSuspendedTransaction {
            return@newSuspendedTransaction ChartTracks.insert {
                it[name] = track.name
                it[playCount] = track.playCount
                it[snapshotId] = track.snapshotId
                it[listeners] = track.listeners
                it[position] = track.position
                it[artist] = track.artist
            }[ChartTracks.id]
        }
    }

    suspend fun getAll(snapshotId: Int): List<ChartTrack> {
        return newSuspendedTransaction {
            return@newSuspendedTransaction ChartTracks.select {
                ChartTracks.snapshotId eq snapshotId
            }.map {
                ChartTrack(
                    it[ChartTracks.playCount],
                    it[ChartTracks.snapshotId],
                    it[ChartTracks.listeners],
                    it[ChartTracks.artist],
                    it[ChartTracks.position],
                    it[ChartTracks.name]
                )
            }
        }
    }
}
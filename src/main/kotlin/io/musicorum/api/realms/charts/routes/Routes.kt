package io.musicorum.api.realms.charts.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.charts.repositories.ChartSnapshotRepository
import io.musicorum.api.realms.charts.repositories.ChartTrackRepository
import io.musicorum.api.realms.charts.repositories.lastfm.LastFmChartRepository
import io.musicorum.api.realms.charts.schemas.ChartTrack
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Application.createChartRoutes() {
    val chartSnapshotRepository = inject<ChartSnapshotRepository>()
    val trackRepository = inject<ChartTrackRepository>()

    routing {
        route("/charts") {
            route("/positions") {
                createUpdatePositionsRoute()

                get {
                    val snapshotId = call.request.queryParameters["snapshotId"]?.toIntOrNull()
                    if (snapshotId == null) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        val tracks = trackRepository.value.getAll(snapshotId)
                        val lastUpdated = chartSnapshotRepository.value.getSnapshot(snapshotId).updatedAt
                        call.respond(PositionResponse(lastUpdated, tracks))
                    }
                }
            }
        }
    }
}

@Serializable
private data class PositionResponse(
    val updatedAt: Long,
    val trackEntries: List<ChartTrack>
)

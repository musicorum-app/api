package io.musicorum.api.realms.charts.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.charts.repositories.ChartSnapshotRepository
import io.musicorum.api.realms.charts.repositories.ChartTrackRepository
import io.musicorum.api.realms.charts.repositories.lastfm.LastFmChartRepository
import org.koin.ktor.ext.inject

fun Route.createUpdatePositionsRoute() {
    val chartTrackRepository = inject<ChartTrackRepository>()
    val chartSnapshotRepository = inject<ChartSnapshotRepository>()

    route("/update") {
        post {
            val topTracks = LastFmChartRepository.getTopTracks()
            val topArtists = LastFmChartRepository.getTopArtists()

            val snapshotId = chartSnapshotRepository.value.createSnapshot()

            var trackPos = 1
            for (track in topTracks) {
                chartTrackRepository.value.insert(
                    io.musicorum.api.realms.charts.schemas.ChartTrack(
                        name = track.name,
                        playCount = track.playCount.toLong(),
                        listeners = track.listeners.toLong(),
                        position = trackPos++,
                        artist = track.artist.name,
                        snapshotId = snapshotId
                    )
                )
            }

            call.respond(HttpStatusCode.Created)
        }
    }
}
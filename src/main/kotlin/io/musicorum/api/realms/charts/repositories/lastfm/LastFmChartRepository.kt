package io.musicorum.api.realms.charts.repositories.lastfm

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.charts.schemas.lastfm.BaseChartArtistResponse
import io.musicorum.api.realms.charts.schemas.lastfm.BaseChartTrackResponse
import io.musicorum.api.realms.charts.schemas.lastfm.ChartArtist
import io.musicorum.api.realms.charts.schemas.lastfm.ChartTrack
import io.musicorum.api.utils.getRequiredEnv
import kotlinx.serialization.json.Json

class LastFmChartRepository {
    companion object {
        private val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            defaultRequest {
                url("https", "ws.audioscrobbler.com") {
                    path("2.0")
                    parameters.append("api_key", getRequiredEnv(EnvironmentVariable.LastfmApiKey))
                    parameters.append("format", "json")

                }
            }
        }

        suspend fun getTopTracks(): List<ChartTrack> {
            val res = client.get {
                url {
                    parameter("method", "chart.gettoptracks")
                }
            }.body<BaseChartTrackResponse>()

            return res.tracks.tracks
        }

        suspend fun getTopArtists(): List<ChartArtist> {
            val res = client.get {
                url {
                    parameter("method", "chart.gettopartists")
                }
            }.body<BaseChartArtistResponse>()

            return res.artists.artists
        }
    }
}
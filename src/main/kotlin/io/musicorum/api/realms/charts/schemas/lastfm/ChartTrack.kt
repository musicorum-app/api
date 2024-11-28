package io.musicorum.api.realms.charts.schemas.lastfm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseChartTrackResponse(
    val tracks: ChartTrackResponse
)

@Serializable
data class ChartTrackResponse(
    @SerialName("track")
    val tracks: List<ChartTrack>
)

@Serializable
data class ChartTrack(
    val name: String,
    @SerialName("playcount")
    val playCount: String,
    val listeners: String,
    val artist: Artist
)

@Serializable
data class Artist(
    val name: String
)

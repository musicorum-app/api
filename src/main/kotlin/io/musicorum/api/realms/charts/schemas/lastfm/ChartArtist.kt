package io.musicorum.api.realms.charts.schemas.lastfm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseChartArtistResponse(
    val artists: ChartArtistResponse
)

@Serializable
data class ChartArtistResponse(
    @SerialName("artist")
    val artists: List<ChartArtist>
)


@Serializable
data class ChartArtist(
    val name: String,
    @SerialName("playcount")
    val playCount: String,
    val listeners: String
)
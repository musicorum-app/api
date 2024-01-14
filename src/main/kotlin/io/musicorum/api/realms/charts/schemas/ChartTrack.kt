package io.musicorum.api.realms.charts.schemas

import kotlinx.serialization.Serializable
import java.math.BigInteger

@Serializable
data class ChartTrack(
    val playCount: Long,
    val snapshotId: Int,
    val listeners: Long,
    val artist: String,
    val position: Int,
    val name: String
)
package io.musicorum.api.realms.charts.schemas

import kotlinx.serialization.Serializable

@Serializable
data class ChartSnapshot(
    val id: Int,
    val updatedAt: Long
)

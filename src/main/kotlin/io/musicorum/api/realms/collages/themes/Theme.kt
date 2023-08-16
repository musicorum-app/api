package io.musicorum.api.realms.collages.themes

import io.musicorum.api.realms.collages.themes.serialization.ThemeData
import kotlinx.serialization.Serializable

interface Theme {
    val name: String

    suspend fun handleGenerationData(data: CollagePayload): IWorkerData

    @Serializable
    data class CollagePayload(
        val user: String,
        val theme: ThemeData,
        val hideUsername: Boolean,
    )

    @Serializable
    sealed interface IWorkerData

    @Serializable
    sealed interface IGenerationData
}
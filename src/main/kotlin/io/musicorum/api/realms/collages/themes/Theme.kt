package io.musicorum.api.realms.collages.themes

import kotlinx.serialization.Serializable

interface Theme {
    val name: String

    suspend fun handleGenerationData(data: CollagePayload<IGenerationData>): IWorkerData

    @Serializable
    data class CollagePayload<T>(
        val user: String,
        val theme: String,
        val options: T,
        val hideUsername: Boolean,
    )

    @Serializable
    sealed interface IWorkerData

    @Serializable
    sealed interface IGenerationData
}
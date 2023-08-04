package io.musicorum.api.realms.collages.themes

import kotlinx.serialization.Serializable

abstract class Theme<WORKER_DATA: Theme.IWorkerData, GENERATION_DATA: Theme.IGenerationData> {
    abstract val name: String

    abstract suspend fun handleGenerationData(data: CollagePayload<GENERATION_DATA>): WORKER_DATA

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
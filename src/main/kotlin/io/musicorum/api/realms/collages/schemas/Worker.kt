package io.musicorum.api.realms.collages.schemas

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.collages.themes.ThemeEnum
import io.musicorum.api.realms.resources.services.jsonConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Worker(
        val url: String
) {
    var engine: String? = null
    var name: String? = null
    val availableThemes = ArrayList<String>()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        defaultRequest {
            url(this@Worker.url)
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun connect() {
        val response = client.get("/metadata")

        if (response.status.isSuccess()) {
            val metadata = response.body<MetadataResponse>()

            this.engine = metadata.engine
            this.name = metadata.name
            this.availableThemes.addAll(metadata.themes)

            Logger.DEFAULT.log("Worker $name v${metadata.version} (using ${engine}) initialized!")
        } else {
            throw Exception(response.bodyAsText())
        }
    }

    suspend fun generate(payload: WorkerGeneratePayload<Theme.IWorkerData>): WorkerGenerationResponse {
        val response = client.post("/generate") {
            setBody(payload)
        }

        return response.body<WorkerGenerationResponse>()
    }

    fun toSerializable() = SerializableWorker(
            name = name.orEmpty(),
            engine = engine.orEmpty(),
            availableThemes = availableThemes
    )

    @Serializable
    data class SerializableWorker(
            val name: String,
            val engine: String,
            @SerialName("available_themes")
            val availableThemes: List<String>
    )

    @Serializable
    private data class MetadataResponse(
            val name: String,
            val engine: String,
            val scheme: Int,
            val themes: List<String>,
            val version: Int
    )

    @Serializable
    data class WorkerGeneratePayload<T : Theme.IWorkerData>(
            val id: String,
            val theme: ThemeEnum,
            val user: Int?,
            val story: Boolean,
            @SerialName("hide_username")
            val hideUsername: Boolean,
            val data: T
    )

    @Serializable
    data class WorkerGenerationResponse(
            val error: Boolean,
            val file: String,
            val time: Int
    )
}
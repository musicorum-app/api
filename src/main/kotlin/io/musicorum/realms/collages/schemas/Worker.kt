package io.musicorum.realms.collages.schemas

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.realms.resources.services.jsonConfig
import kotlinx.serialization.Serializable
import org.eclipse.jetty.util.ssl.SslContextFactory

data class Worker(
    val url: String
) {
    var engine: String? = null
    var name: String? = null
    val availableThemes = ArrayList<String>()

    private val client = HttpClient(CIO) {
        
        install (Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
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
        println(response)

        if (response.status.isSuccess()) {
            val metadata = response.body<MetadataResponse>()

            this.engine = metadata.engine
            this.name = metadata.name
            this.availableThemes.addAll(metadata.themes)

            Logger.DEFAULT.log("Worker $name initialized!")
        } else {
            throw Exception(response.bodyAsText())
        }
    }

    suspend fun generate() {

    }

    @Serializable
    private data class MetadataResponse(
        val name: String,
        val engine: String,
        val scheme: Int,
        val themes: List<String>,
        val version: Int
    )
}
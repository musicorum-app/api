package io.musicorum.realms.collages.schemas

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

private val client = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
}

data class Worker(
    val engine: String,
    val name: String,
    val url: String
) {
    suspend fun connect() {
        val response = client.post {
            url {
                host = this@Worker.url
                path("metadata")
            }
        }

        response.status
    }

    suspend fun generate() {

    }
}
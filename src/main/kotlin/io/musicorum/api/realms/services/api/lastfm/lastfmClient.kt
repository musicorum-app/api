package io.musicorum.api.realms.services.api.lastfm

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.api.enums.EnvironmentVariable
import kotlinx.serialization.json.Json

private val jsonConfig = Json { ignoreUnknownKeys = true }

private val lastfmKey = System.getenv(EnvironmentVariable.LastfmApiKey)

val lastfmClient = HttpClient {
    defaultRequest {
        url {
            url("https://ws.audioscrobbler.com/2.0")
            parameters.append("format", "json")
            parameters.append("api_key", lastfmKey)
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
    install(ContentNegotiation) {
        json(jsonConfig)
    }
}


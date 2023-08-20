package io.musicorum.lasfmclient

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.lasfmclient.namespaces.UserEndpoint
import kotlinx.serialization.json.Json

internal val jsonConfig = Json { ignoreUnknownKeys = true }

class LastfmClient (
    private val key: String
) {
    val client = HttpClient {
        defaultRequest {
            url {
                url("https://ws.audioscrobbler.com/2.0")
                parameters.append("format", "json")
                parameters.append("api_key", key)
            }
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(jsonConfig)
        }
    }

    val user = UserEndpoint(client)
}


package io.musicorum.realms.resources.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import io.musicorum.enums.EnvironmentVariable
import io.musicorum.realms.resources.exceptions.ResourcesRequestException
import io.musicorum.realms.resources.models.ArtistResource
import io.musicorum.realms.resources.models.ArtistResourcesRequest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*

internal val jsonConfig = Json {
    ignoreUnknownKeys = true
}

class ResourcesService {
    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        defaultRequest {
            url(System.getenv(EnvironmentVariable.ResourcesUrl))
            contentType(ContentType.Application.Json)
        }
    }

    @Throws(ResourcesRequestException::class)
    suspend fun fetchArtistsResources(artists: List<String>): List<ArtistResource> {
        val response = client.post("/find/artists") {
            setBody(ArtistResourcesRequest(artists))
        }

        if (response.status.isSuccess()) {
            return jsonConfig.decodeFromString(
                ListSerializer(ArtistResource.serializer()),
                response.bodyAsText()
            )
        } else {
            throw ResourcesRequestException(response)
        }
    }
}
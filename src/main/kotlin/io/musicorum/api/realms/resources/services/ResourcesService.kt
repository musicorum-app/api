package io.musicorum.api.realms.resources.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.resources.exceptions.ResourcesRequestException
import io.musicorum.api.realms.resources.models.ArtistResource
import io.musicorum.api.realms.resources.models.ArtistResourcesRequest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import io.musicorum.api.realms.resources.models.TrackResource
import io.musicorum.api.realms.resources.models.TrackResourceRequest

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

    private suspend inline fun <reified T> fetchResources(path: String, body: T): HttpResponse {
        val response = client.post(path) {
            setBody(body)
            parameter("sources", "lastfm,spotify,deezer")
        }

        if (response.status.isSuccess()) {
            return response
        } else {
            throw ResourcesRequestException(response)
        }
    }

    @Throws(ResourcesRequestException::class)
    suspend fun fetchArtistsResources(artists: List<String>): List<ArtistResource> {
        val response = fetchResources("/find/artists", ArtistResourcesRequest(artists))

        return jsonConfig.decodeFromString(
            ListSerializer(ArtistResource.serializer()),
            response.bodyAsText()
        )
    }

    @Throws(ResourcesRequestException::class)
    suspend fun fetchTracksResources(tracks: List<TrackResourceRequest.Item>): List<TrackResource> {
        val response = fetchResources("/find/tracks", TrackResourceRequest(tracks))

        return jsonConfig.decodeFromString(
            ListSerializer(TrackResource.serializer()),
            response.bodyAsText()
        )
    }
}
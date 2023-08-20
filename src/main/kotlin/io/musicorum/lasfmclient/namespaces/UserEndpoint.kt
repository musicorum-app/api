package io.musicorum.lasfmclient.namespaces

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.musicorum.api.enums.Period
import io.musicorum.lasfmclient.jsonConfig
import io.musicorum.lasfmclient.schemas.user.UserTopAlbum
import io.musicorum.lasfmclient.schemas.user.UserTopArtist
import io.musicorum.lasfmclient.schemas.user.UserTopTrack
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class UserEndpoint (private val client: HttpClient) {
    suspend fun getTopAlbums(user: String, period: Period = Period.Overall, limit: Int = 20): List<UserTopAlbum> {
        val begin = Clock.System.now()
        val response = client.get {
            parameter("method", "user.getTopAlbums")
            parameter("user", user)
            parameter("period", period.value)
            parameter("limit", limit)
        }
        println("LFM REQ TIME: " + (Clock.System.now().minus(begin).inWholeMilliseconds) + "ms")

        val topAlbums = response.body<UserTopAlbum.UserTopAlbumsResponse>()

        return topAlbums.toAlbumList()
    }

    suspend fun getTopArtists(user: String, period: Period = Period.Overall, limit: Int = 20): List<UserTopArtist> {
        val response = client.get {
            parameter("method", "user.getTopArtists")
            parameter("user", user)
            parameter("period", period.value)
            parameter("limit", limit)
        }

        val responseObject = response.body<JsonObject>()

        val topArtists = responseObject["topartists"] as JsonObject
        val list = topArtists["artist"]!!
        val parsed = jsonConfig.decodeFromJsonElement<List<UserTopArtist.ResponseItem>>(list)

        return parsed.map{ it.toTopArtist() }
    }

    suspend fun getTopTracks(user: String, period: Period = Period.Overall, limit: Int = 20): List<UserTopTrack> {
        val response = client.get {
            parameter("method", "user.getTopTracks")
            parameter("user", user)
            parameter("period", period.value)
            parameter("limit", limit)
        }

        val responseObject = response.body<JsonObject>()

        val topTracks = responseObject["toptracks"] as JsonObject
        val list = topTracks["track"]!!
        val parsed = jsonConfig.decodeFromJsonElement<List<UserTopTrack.ResponseItem>>(list)

        return parsed.map{ it.toTopTrack() }
    }
}

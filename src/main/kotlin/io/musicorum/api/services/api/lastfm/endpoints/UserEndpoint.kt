package io.musicorum.api.services.api.lastfm.endpoints

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.musicorum.api.enums.Period
import io.musicorum.api.services.api.lastfm.lastfmClient
import io.musicorum.api.services.api.lastfm.schemas.album.UserTopAlbum

object UserEndpoint {
    suspend fun getTopAlbums(user: String, period: Period = Period.Overall): List<UserTopAlbum> {
        val response = lastfmClient.get {
            parameter("method", "user.getTopAlbums")
            parameter("user", user)
            parameter("period", period.value)
        }

        val topAlbums = response.body<UserTopAlbum.UserTopAlbumsResponse>()

        return topAlbums.toAlbumList()
    }
}

package io.musicorum.api.realms.services.api.lastfm.endpoints

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.musicorum.api.enums.Period
import io.musicorum.api.realms.services.api.lastfm.lastfmClient
import io.musicorum.api.realms.services.api.lastfm.schemas.album.UserTopAlbum
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object UserEndpoint {
    suspend fun getTopAlbums(user: String, period: Period = Period.Overall, limit: Int = 20): List<UserTopAlbum> {
        val begin = Clock.System.now()
        val response = lastfmClient.get {
            parameter("method", "user.getTopAlbums")
            parameter("user", user)
            parameter("period", period.value)
            parameter("limit", limit)
        }
        println("LFM REQ TIME: " + (Clock.System.now().minus(begin).inWholeMilliseconds) + "ms")


        val topAlbums = response.body<UserTopAlbum.UserTopAlbumsResponse>()


        return topAlbums.toAlbumList()
    }
}

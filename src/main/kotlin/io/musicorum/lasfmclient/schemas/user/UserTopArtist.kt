package io.musicorum.lasfmclient.schemas.user

import io.musicorum.lasfmclient.schemas.Artist
import kotlinx.serialization.Serializable

@Serializable
data class UserTopArtist(
    override val name: String,
    override val url: String,
    val playCount: Int
): Artist {
    @Serializable
    data class ResponseItem(
        val name: String,
        val url: String,
        val playcount: String,
    ) {
        fun toTopArtist() = UserTopArtist(
            name = this.name,
            url = this.url,
            playCount = this.playcount.toInt()
        )
    }
}



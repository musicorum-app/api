package io.musicorum.lasfmclient.schemas.user

import io.musicorum.lasfmclient.schemas.Track
import kotlinx.serialization.Serializable

@Serializable
data class UserTopTrack(
    override val name: String,
    override val url: String,
    override val artistName: String,
    val playCount: Int
): Track {
    @Serializable
    data class ResponseItem(
        val name: String,
        val url: String,
        val playcount: String,
        val artist: ArtistItem
    ) {
        @Serializable
        data class ArtistItem (
            val name: String
        )

        fun toTopTrack() = UserTopTrack(
            name = this.name,
            url = this.url,
            artistName = this.artist.name,
            playCount = this.playcount.toInt()
        )
    }
}
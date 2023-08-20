package io.musicorum.lasfmclient.schemas.user

import io.musicorum.lasfmclient.schemas.Image
import io.musicorum.lasfmclient.schemas.Album
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserTopAlbum(
    override val name: String,
    override val url: String,
    val playCount: Int,
    override val images: List<Image>
): Album {
    @Serializable
    internal data class UserTopAlbumsResponse (
        @SerialName("topalbums")
        private val topAlbums: TopAlbums
    ) {
        @Serializable
        data class TopAlbums(
            @SerialName("album")
            val albums: List<TopAlbum>
        )
        @Serializable
        data class TopAlbum(
            val name: String,
            val playcount: String,
            val url: String,
            val image: List<Image>
        )

        fun toAlbumList(): List<UserTopAlbum> {
            return topAlbums.albums.map {
                UserTopAlbum(
                    name = it.name,
                    playCount = it.playcount.toInt(),
                    images = it.image,
                    url = it.url
                )
            }
        }
    }
}

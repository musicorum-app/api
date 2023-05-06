package io.musicorum.realms.resources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResource(
    val hash: String,
    val explicit: Boolean?,
    val active: Boolean,
    val source: ImageSource,
    val images: List<Image>,
    @SerialName("color_palette")
    val colorPalette: ColorPalette,
    @SerialName("created_at")
    val createdAt: String,
) {
    companion object {
        @Serializable
        enum class ImageSource {
            SPOTIFY,
            LASTFM,
            DEEZER
        }

        @Serializable
        enum class ImageSize {
            EXTRA_SMALL,
            SMALL,
            MEDIUM,
            LARGE,
            EXTRA_LARGE
        }

        @Serializable
        data class Image(
            val hash: String,
            val size: ImageSize,
            val url: String
        )

        @Serializable
        data class ColorPalette(
            val vibrant: String?,
            @SerialName("dark_vibrant")
            val darkVibrant: String?,
            @SerialName("light_vibrant")
            val lightVibrant: String?,
            val muted: String?,
            @SerialName("dark_muted")
            val darkMuted: String?,
            @SerialName("light_muted")
            val lightMuted: String?
        )
    }
}

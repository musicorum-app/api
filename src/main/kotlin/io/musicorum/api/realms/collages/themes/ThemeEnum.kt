package io.musicorum.api.realms.collages.themes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ThemeEnum (val themeName: String) {
    @SerialName("classic_collage")
    ClassicCollage("classic_collage")
}
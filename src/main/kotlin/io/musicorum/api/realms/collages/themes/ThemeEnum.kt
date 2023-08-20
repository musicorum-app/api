package io.musicorum.api.realms.collages.themes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ThemeEnum (val themeName: String) {
    @SerialName("classic_collage")
    ClassicCollage("classic_collage");

    companion object {
        fun fromThemeName(themeName: String): ThemeEnum? {
            return ThemeEnum.values().find { it.themeName == themeName }
        }
    }
}
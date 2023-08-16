package io.musicorum.api.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Entity {
    @SerialName("ARTIST")
    Artist,
    @SerialName("TRACK")
    Track,
    @SerialName("ALBUM")
    Album
}
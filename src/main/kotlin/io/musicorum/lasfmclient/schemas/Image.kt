package io.musicorum.lasfmclient.schemas

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val size: String,
    @SerialName("#text")
    val url: String?
)

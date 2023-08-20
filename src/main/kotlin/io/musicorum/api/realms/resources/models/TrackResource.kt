package io.musicorum.api.realms.resources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackResource(
    override val hash: String,
    override val name: String,
    val album: String?,
    val artists: List<String>?,
    override val resources: List<ImageResource>,
    @SerialName("preferred_resource")
    override val preferredResource: String?,
    @SerialName("created_at")
    override val createdAt: String,
    @SerialName("updated_at")
    override val updatedAt: String?
): ResourceItem()

@Serializable
data class TrackResourceRequest(
    val tracks: List<Item>
) {
    @Serializable
    data class Item(
        val name: String,
        val artist: String
    )
}
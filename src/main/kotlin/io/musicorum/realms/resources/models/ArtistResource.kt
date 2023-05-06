package io.musicorum.realms.resources.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistResource(
    override val hash: String,
    override val name: String,
    override val resources: List<ImageResource>,
    @SerialName("preferred_resource")
    override val preferredResource: String?,
    @SerialName("created_at")
    override val createdAt: String,
    @SerialName("updated_at")
    override val updatedAt: String?
): ResourceItem

@Serializable
data class ArtistResourcesRequest(
    val artists: List<String>
)
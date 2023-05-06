package io.musicorum.realms.resources.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ResourceItem {
    val hash: String
    val name: String

    val resources: List<ImageResource>
    val preferredResource: String?

    val createdAt: String
    val updatedAt: String?
}
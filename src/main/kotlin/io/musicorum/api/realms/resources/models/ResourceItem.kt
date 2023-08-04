package io.musicorum.api.realms.resources.models

interface ResourceItem {
    val hash: String
    val name: String

    val resources: List<ImageResource>
    val preferredResource: String?

    val createdAt: String
    val updatedAt: String?
}
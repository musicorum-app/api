package io.musicorum.api.realms.resources.models

abstract class ResourceItem {
    abstract val hash: String
    abstract val name: String

    abstract val resources: List<ImageResource>
    abstract val preferredResource: String?

    abstract val createdAt: String
    abstract val updatedAt: String?

    fun getLastfmResourceOrOther(): ImageResource? {
        val lastfmResource = this.resources.find { it.source === ImageResource.ImageSource.LASTFM }
        return lastfmResource ?: this.resources.getOrNull(0)
    }
}
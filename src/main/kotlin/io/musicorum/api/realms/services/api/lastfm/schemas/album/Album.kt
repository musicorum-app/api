package io.musicorum.api.realms.services.api.lastfm.schemas.album

import io.musicorum.api.realms.services.api.lastfm.schemas.Image

interface Album {
    val name: String
    val url: String
    val images: List<Image>

}
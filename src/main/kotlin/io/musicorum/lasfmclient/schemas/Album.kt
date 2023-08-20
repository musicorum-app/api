package io.musicorum.lasfmclient.schemas

import io.musicorum.lasfmclient.schemas.Image

interface Album {
    val name: String
    val url: String
    val images: List<Image>
}
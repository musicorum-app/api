package io.musicorum.api.realms.collages.themes

import io.musicorum.api.enums.Entity
import io.musicorum.api.enums.Period
import io.musicorum.api.realms.resources.services.ResourcesService
import io.musicorum.api.services.lastfmClient
import io.musicorum.lasfmclient.endpoints.UserEndpoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GridTheme(
    val resourcesService: ResourcesService,
) : Theme {
    override val name = "classic_collage"
    override suspend fun handleGenerationData(data: Theme.CollagePayload): Theme.IWorkerData {
        val tiles: List<GridTile>
        val options = data.theme.options as GenerationData

        val size = options.columns * options.rows

        if (options.entity == Entity.Album) {
            val albums = lastfmClient.user.getTopAlbums(data.user, options.period, size)

            tiles = albums.map {
                GridTile(
                    name = it.name,
                    image = it.images[3].url,
                    playCount = it.playCount,
                )
            }
        } else {
            throw NotImplementedError("This entity is not implemented for this theme")
        }

        return WorkerData(
            tiles = tiles,
            style = options.style,
            columns = options.columns,
            rows = options.rows,
            padded = options.padded,
            showNames = options.showNames,
            showPlayCount = options.showPlayCount,
            tileSize = 300
        )
    }

    @Serializable
    data class GridTile(
        val image: String?,
        val name: String,
        val secondary: String? = null,
        val playCount: Int? = null
    )

    @Serializable
    data class WorkerData(
        val tiles: List<GridTile>,
        val padded: Boolean,
        val rows: Int,
        val columns: Int,
        @SerialName("show_names")
        val showNames: Boolean,
        @SerialName("show_playcount")
        val showPlayCount: Boolean,
        val style: String,
        val tileSize: Int
    ) : Theme.IWorkerData

    @Serializable
    data class GenerationData(
        val rows: Int,
        val columns: Int,
        @SerialName("show_names")
        val showNames: Boolean,
        @SerialName("show_playcount")
        val showPlayCount: Boolean,
        val style: String,
        val padded: Boolean,
        val period: Period,
        val entity: Entity,
    ): Theme.IGenerationData
}
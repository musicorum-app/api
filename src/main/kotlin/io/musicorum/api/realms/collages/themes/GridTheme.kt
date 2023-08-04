package io.musicorum.api.realms.collages.themes

import io.musicorum.api.enums.Entity
import io.musicorum.api.enums.Period
import io.musicorum.api.realms.resources.services.ResourcesService
import io.musicorum.api.services.api.lastfm.endpoints.UserEndpoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GridTheme(
    val resourcesService: ResourcesService,
) : Theme<GridTheme.IWorkerData, GridTheme.GenerationData>() {
    override val name = "grid"
    override suspend fun handleGenerationData(data: CollagePayload<GenerationData>): IWorkerData {
        val tiles: List<GridTile>

        if (data.options.entity == Entity.Album) {
            val albums = UserEndpoint.getTopAlbums(data.user, data.options.period)

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

        return IWorkerData(
            tiles = tiles,
            style = data.options.style,
            columns = data.options.columns,
            rows = data.options.rows,
            padded = data.options.padded,
            showNames = data.options.showNames,
            showPlayCount = data.options.showPlayCount,
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
    data class IWorkerData(
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
    ): IGenerationData
}
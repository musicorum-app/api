package io.musicorum.api.realms.collages.themes

import io.musicorum.api.enums.Entity
import io.musicorum.api.enums.Period
import io.musicorum.api.realms.resources.models.TrackResourceRequest
import io.musicorum.api.realms.resources.services.ResourcesService
import io.musicorum.api.services.lastfmClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GridTheme(
    private val resourcesService: ResourcesService,
) : Theme {
    override val name = "classic_collage"
    override suspend fun handleGenerationData(data: Theme.CollagePayload): Theme.IWorkerData {
        val tiles: List<GridTile>
        val options = data.theme.options as GenerationData

        val size = options.columns * options.rows

        println(options)

        if (options.entity == Entity.Album) {
            val albums = lastfmClient.user.getTopAlbums(data.user, options.period, size)

            tiles = albums.map {
                GridTile(
                    name = it.name,
                    image = it.images[3].url,
                    playCount = it.playCount,
                )
            }
        } else if (options.entity == Entity.Artist) {
            val artists = lastfmClient.user.getTopArtists(data.user, options.period, size)

            val resources = resourcesService.fetchArtistsResources(artists.map { it.name })

            tiles = artists.mapIndexed { index, artist ->
                val resource = resources.getOrNull(index)?.getLastfmResourceOrOther()
                GridTile(
                    name = artist.name,
                    image = resource?.images?.getOrNull(0)?.url,
                    playCount = artist.playCount
                )
            }
        } else if (options.entity == Entity.Track) {
            val tracks = lastfmClient.user.getTopTracks(data.user, options.period, size)

            val resources = resourcesService.fetchTracksResources(tracks.map {
                TrackResourceRequest.Item(
                    name = it.name,
                    artist = it.artistName
                )
            })

            tiles = tracks.mapIndexed { index, track ->
                val resource = resources.getOrNull(index)?.getLastfmResourceOrOther()
                GridTile(
                    name = track.name,
                    image = resource?.images?.getOrNull(0)?.url ?: "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    playCount = track.playCount
                )
            }

            println(tiles)
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
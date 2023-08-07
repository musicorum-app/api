package io.musicorum.api.realms.collages.services

import io.ktor.server.application.*
import io.ktor.util.logging.*
import io.musicorum.api.enums.Entity
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.enums.Period
import io.musicorum.api.generateNanoId
import io.musicorum.api.realms.collages.schemas.Worker
import io.musicorum.api.realms.collages.themes.GridTheme
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.resources.services.ResourcesService
import kotlinx.datetime.Clock
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

private val LOGGER = KtorSimpleLogger("io.musicorum.api.realms.collages.services.CollagesService")

class CollagesService(private val workersService: WorkersService, private val resourcesService: ResourcesService) {
    private val gridTheme = GridTheme(resourcesService)

    suspend fun create(data: Theme.CollagePayload<Theme.IGenerationData>): CollageResponse {
        val start = Clock.System.now()
        val id = generateNanoId(32)

        val themeName = data.theme
        val theme = getTheme(themeName)

        val worker = workersService.getWorkerForTheme(themeName) ?: throw Exception("No available worker for this theme")
        val workerData = theme.handleGenerationData(data)

        val payload = Worker.WorkerGeneratePayload(
            theme = themeName,
            hideUsername = false,
            user = null,
            id = id,
            story = false,
            data = workerData
        )

        val generationStart = Clock.System.now()

        val generation = worker.generate(payload)

        val end = Clock.System.now()
        val generationDuration = (end - generationStart).inWholeMilliseconds
        val totalDuration = (end - start).inWholeMilliseconds

        LOGGER.info("Generation for theme $theme took ${totalDuration}ms (${generationDuration}ms on worker)")

        val resultUrl = System.getenv(EnvironmentVariable.ResultUrl) + generation.file

        return CollageResponse(
            duration = totalDuration,
            file = generation.file,
            id = id,
            url = resultUrl
        )
    }

    private fun getTheme(themeName: String): Theme {
        return when (themeName) {
            "classic_grid" -> gridTheme
            else -> throw InvalidBodyException("This theme does not exist")
        }
    }

    @Serializable
    data class CollageResponse (
        val duration: Long,
        val file: String,
        val id: String,
        val url: String,
    )
}
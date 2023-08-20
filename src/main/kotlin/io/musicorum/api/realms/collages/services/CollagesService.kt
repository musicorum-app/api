package io.musicorum.api.realms.collages.services

import io.ktor.util.logging.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.collages.repositories.CollagesRepository
import io.musicorum.api.utils.generateNanoId
import io.musicorum.api.realms.collages.schemas.Worker
import io.musicorum.api.realms.collages.themes.GridTheme
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.collages.themes.ThemeEnum
import io.musicorum.api.realms.resources.services.ResourcesService
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

private val LOGGER = KtorSimpleLogger("io.musicorum.api.realms.collages.services.CollagesService")

class CollagesService(
    private val workersService: WorkersService,
    private val resourcesService: ResourcesService,
    private val collagesRepository: CollagesRepository
) {
    private val gridTheme = GridTheme(resourcesService)

    suspend fun create(data: Theme.CollagePayload): CollageResponse {
        val start = Clock.System.now()
        val id = generateNanoId(32)

        val theme = data.theme.name
        val themeImpl = getTheme(theme)

        val worker =
            workersService.getWorkerForTheme(theme) ?: throw Exception("No available worker for this theme")
        val workerData = themeImpl.handleGenerationData(data)

        val payload = Worker.WorkerGeneratePayload(
            theme = theme,
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

        LOGGER.info("Generation for theme $themeImpl took ${totalDuration}ms (${generationDuration}ms on worker)")

        val resultUrl = System.getenv(EnvironmentVariable.ResultUrl) + generation.file

        collagesRepository.createOne(id, theme, generation.file, generationDuration)

        return CollageResponse(
            duration = totalDuration,
            file = generation.file,
            id = id,
            url = resultUrl
        )
    }

    private fun getTheme(theme: ThemeEnum): Theme {
        return when (theme) {
            ThemeEnum.ClassicCollage -> gridTheme
        }
    }

    @Serializable
    data class CollageResponse(
        val duration: Long,
        val file: String,
        val id: String,
        val url: String,
    )
}
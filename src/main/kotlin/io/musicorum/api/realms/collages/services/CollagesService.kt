package io.musicorum.api.realms.collages.services

import io.musicorum.api.enums.Entity
import io.musicorum.api.enums.Period
import io.musicorum.api.realms.collages.schemas.Worker
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.collages.themes.GridTheme
import io.musicorum.api.realms.resources.services.ResourcesService


class CollagesService(private val workersService: WorkersService, private val resourcesService: ResourcesService) {
    val gridTheme = GridTheme(resourcesService)
    suspend fun create(theme: String): Worker.WorkerGeneratePayload<Theme.IWorkerData> {
        val selectedTheme = gridTheme
//        val worker = workersService.getWorkerForTheme(theme) ?: throw Exception("No available worker for this theme")

        val data = selectedTheme.handleGenerationData(
            Theme.CollagePayload(
                user = "metye",
                theme = "grid",
                options = GridTheme.GenerationData(
                    style = "STYLE",
                    showNames = true,
                    showPlayCount = true,
                    padded = false,
                    columns = 10,
                    rows = 10,
                    entity = Entity.Album,
                    period = Period.Overall
                ),
                hideUsername = false,
            )
        ) as Theme.IWorkerData

        val payload = Worker.WorkerGeneratePayload(
            theme = theme,
            hideUsername = false,
            user = null,
            id = "SOME-ID",
            story = false,
            data = data
        )

//        worker.generate(payload)

        return payload
    }
}
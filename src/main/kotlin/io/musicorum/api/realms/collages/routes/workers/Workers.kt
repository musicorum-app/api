package io.musicorum.api.realms.collages.routes.workers

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.collages.schemas.Worker
import io.musicorum.api.realms.collages.services.WorkersService
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.createWorkersRoute() {
    val workersService = inject<WorkersService>()

    get {
        call.respond(
                WorkersResponse(
                        workersService.value.workers.map { it.toSerializable() }
                )
        )
    }
}

@Serializable
data class WorkersResponse(
        val workers: List<Worker.SerializableWorker>
)
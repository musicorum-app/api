package io.musicorum.api.realms.collages

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.collages.services.CollagesService
import org.koin.ktor.ext.inject

fun Application.createCollagesRoutes() {
    val collagesService = inject<CollagesService>()

    routing {
        route("/collages") {
            get {
                val data = collagesService.value.create("grid")

                call.respond(data)
            }
        }
    }
}
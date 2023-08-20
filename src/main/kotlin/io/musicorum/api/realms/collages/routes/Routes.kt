package io.musicorum.api.realms.collages.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.collages.routes.collages.createCollageRoute
import io.musicorum.api.realms.collages.routes.workers.createWorkersRoute

fun Application.createCollagesRoutes() {
    routing {
        route("/collages") {
            this.createCollageRoute()
        }
        route("/workers"){
            this.createWorkersRoute()
        }
    }
}
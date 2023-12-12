package io.musicorum.api.realms.docs

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.createDocsRoute() {
    routing {
        swaggerUI(path = "documentation", swaggerFile = "openapi/documentation.yaml")
    }
}
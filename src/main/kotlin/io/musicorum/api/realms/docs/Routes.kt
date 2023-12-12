package io.musicorum.api.realms.docs

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.swagger.codegen.languages.StaticHtml2Generator
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen

fun Application.createDocsRoute() {
    routing {
        swaggerUI(path = "documentation", swaggerFile = "openapi/documentation.yaml")
    }
}
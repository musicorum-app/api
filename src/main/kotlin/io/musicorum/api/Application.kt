package io.musicorum.api

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.musicorum.api.plugins.configureSerialization
import io.musicorum.api.plugins.installStatusPages
import io.musicorum.api.koin.installKoin
import io.musicorum.api.plugins.configureHTTP
import io.musicorum.api.realms.auth.createAuthRoutes
import io.musicorum.api.realms.charts.routes.createChartRoutes
import io.musicorum.api.realms.collages.routes.createCollagesRoutes
import io.musicorum.api.realms.docs.createDocsRoute
import io.musicorum.api.realms.resources.createResourcesRoutes
import io.musicorum.api.security.configureSecurity

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    installKoin()
    configureSecurity()
    configureHTTP()
    configureSerialization()
//    configureDatabases()

    installStatusPages()

    createResourcesRoutes()
    createAuthRoutes()
    createCollagesRoutes()
    createDocsRoute()
    createChartRoutes()
}

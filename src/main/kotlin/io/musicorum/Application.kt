package io.musicorum

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.musicorum.koin.installKoin
import io.musicorum.plugins.*
import io.musicorum.realms.auth.createAuthRoutes
import io.musicorum.realms.resources.createResourcesRoutes
import io.musicorum.security.configureSecurity

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
    configureRouting()

    createResourcesRoutes()
    createAuthRoutes()

}

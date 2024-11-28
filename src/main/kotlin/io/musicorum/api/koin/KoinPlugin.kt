package io.musicorum.api.koin

import io.ktor.server.application.*
import io.musicorum.api.koin.modules.mainModule
import io.musicorum.api.realms.auth.authModule
import io.musicorum.api.realms.charts.chartModules
import io.musicorum.api.realms.collages.collagesModule
import io.musicorum.api.realms.resources.resourcesModule
import org.koin.ktor.plugin.Koin

fun Application.installKoin() {
    install(Koin) {
        modules(mainModule)

        modules(authModule)

        modules(resourcesModule)

        modules(collagesModule)

        modules(chartModules)
    }
}
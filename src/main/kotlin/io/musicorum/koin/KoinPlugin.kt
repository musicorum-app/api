package io.musicorum.koin

import io.ktor.server.application.*
import io.musicorum.koin.modules.databaseModule
import io.musicorum.realms.auth.authModule
import org.koin.ktor.plugin.Koin

fun Application.installKoin() {
    install(Koin) {
        modules(databaseModule)

        modules(authModule)
    }
}
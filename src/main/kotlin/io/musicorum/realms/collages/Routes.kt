package io.musicorum.realms.collages

import io.ktor.server.application.*
import io.musicorum.realms.collages.services.WorkersService
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.createCollagesRoutes() {
    val workersService = inject<WorkersService>()

    println(workersService.value.workers)
}
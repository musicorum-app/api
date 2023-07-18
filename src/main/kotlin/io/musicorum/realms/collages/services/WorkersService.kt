package io.musicorum.realms.collages.services

import io.ktor.util.logging.*
import io.ktor.utils.io.*
import io.musicorum.enums.EnvironmentVariable
import io.musicorum.realms.collages.schemas.Worker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

internal val LOGGER = KtorSimpleLogger("io.musicorum.realms.collages.services.WorkersService")

class WorkersService {
    val workers = ArrayList<Worker>()

    init {
        val workersUrls = System.getenv(EnvironmentVariable.WorkersUrls).split(";")

        workers.addAll(
            workersUrls.map { Worker(it) }
        )

        GlobalScope.launch {
            initWorkers()
        }
    }

    suspend fun initWorkers() {
        LOGGER.info("Initializing workers")
        for (worker in workers) {
            try {
                worker.connect()
            } catch (exception: Exception) {
                LOGGER.error("Could not connect to worker at ${worker.url} because:")
                exception.printStack()
            }
        }
    }
}
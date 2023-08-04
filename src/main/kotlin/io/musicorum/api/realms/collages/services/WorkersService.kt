package io.musicorum.api.realms.collages.services

import io.ktor.util.logging.*
import io.ktor.utils.io.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.collages.schemas.Worker
import kotlinx.coroutines.*

internal val LOGGER = KtorSimpleLogger("io.musicorum.realms.collages.services.WorkersService")

class WorkersService {
    val workers = ArrayList<Worker>()

    init {
        val workersEnv = System.getenv(EnvironmentVariable.WorkersUrls)
        if (workersEnv != null) {
            val workersUrls = workersEnv.split(";")

            workers.addAll(
                workersUrls.map { Worker(it) }
            )

            initWorkers()
        } else {
            LOGGER.warn("${EnvironmentVariable.WorkersUrls} is not defined. Ignoring workers initialization")
        }
    }

    private fun initWorkers() {
        LOGGER.info("Initializing workers")
        CoroutineScope(Dispatchers.Default).launch {
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

    fun getWorkerForTheme(theme: String): Worker? {
        // @todo: use a better matcher for the worker, instead of just getting the first
        return this.workers.find { it.availableThemes.contains(theme) }
    }
}
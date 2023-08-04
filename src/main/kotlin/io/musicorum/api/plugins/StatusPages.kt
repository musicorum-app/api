package io.musicorum.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

fun Application.installStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }

        exception<Throwable> { call, cause ->
            call.respond(
                ExceptionResponse(cause.message ?: "Unknown error")
            )
        }
    }
}

@Serializable
internal data class ExceptionResponse(val message: String)
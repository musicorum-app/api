package io.musicorum.api.plugins

import io.ktor.http.*
import io.ktor.serialization.*
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
            cause.printStackTrace()
            if (cause.cause is JsonConvertException) {
                println(cause.message)
                println(cause.cause)
                return@exception call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse("Serialization error: " + cause.message)
                )
            }
            call.respond(
                HttpStatusCode.BadRequest,
                ExceptionResponse(cause.message ?: "Unknown error")
            )
        }
    }
}

@Serializable
internal data class ExceptionResponse(val message: String)
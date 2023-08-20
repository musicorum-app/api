package io.musicorum.api.realms.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.auth.services.ClientService
import io.musicorum.api.security.AuthenticationMethod
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class ClientCreationDAO(
    val name: String
)

fun Application.createAuthRoutes() {
    val clientService = inject<ClientService>()

    routing {
        authenticate(AuthenticationMethod.Super) {
            post("/clients") {
                val obj = call.receive<ClientCreationDAO>()
                val client = clientService.value.createOne(obj.name)

                call.respond(client)
            }
        }
    }
}
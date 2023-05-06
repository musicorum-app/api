package io.musicorum.realms.resources

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.realms.resources.models.ArtistResourcesRequest
import io.musicorum.realms.resources.services.ResourcesService
import io.musicorum.security.AuthenticationMethod
import org.koin.ktor.ext.inject

fun Application.createResourcesRoutes() {
    val resourcesService = inject<ResourcesService>()

    routing {
        authenticate(AuthenticationMethod.Super, AuthenticationMethod.Client) {
            route("/resources") {
                post("/artists") {
                    val body = call.receive<ArtistResourcesRequest>()

                    println(body)

                    val resources = resourcesService.value.fetchArtistsResources(body.artists)

                    call.respond(resources)
                }
            }
        }
    }
}